package com.shishusneh.data.repository

import com.shishusneh.data.constants.FeedingTips
import com.shishusneh.data.local.dao.GenAIConversationDao
import com.shishusneh.data.local.entity.GenAIConversationEntity
import com.shishusneh.data.remote.buildGeminiRequest
import com.shishusneh.data.remote.GeminiApiService
import com.shishusneh.domain.model.GenAIConversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class GenAIRepository @Inject constructor(
    private val dao: GenAIConversationDao,
    private val apiService: GeminiApiService?
) {
    fun getConversations(babyId: Long): Flow<List<GenAIConversation>> =
        dao.getAllConversations(babyId).map { list -> list.map { it.toDomain() } }

    fun getRecentConversations(babyId: Long, limit: Int = 20): Flow<List<GenAIConversation>> =
        dao.getRecentConversations(babyId, limit).map { list -> list.map { it.toDomain() } }

    suspend fun updateFeedback(id: Long, rating: Int) =
        dao.updateFeedback(id, rating)

    /**
     * Ask a question to the AI assistant.
     * Tries online API first, falls back to cached myth-buster Q&A.
     */
    suspend fun askQuestion(
        babyId: Long,
        query: String,
        language: String,
        babyAgeWeeks: Int
    ): GenAIConversation {
        // Try cached myth-buster answers first (instant, offline)
        val cachedAnswer = findCachedAnswer(query, language)
        if (cachedAnswer != null) {
            val conversation = GenAIConversation(
                babyId = babyId,
                timestamp = System.currentTimeMillis(),
                userQuery = query,
                aiResponse = cachedAnswer,
                language = language,
                isFromCache = true
            )
            val id = dao.insert(conversation.toEntity())
            return conversation.copy(id = id)
        }

        // Try online API if available — with retry for rate limits
        if (apiService == null) {
            Log.e("GenAIRepository", "apiService is NULL — API key may be blank or Retrofit init failed")
        } else {
            Log.d("GenAIRepository", "apiService is available, making API call...")
            val prompt = buildPrompt(query, language, babyAgeWeeks)
            val request = buildGeminiRequest(prompt)
            var lastException: Exception? = null
            var lastErrorBody: String? = null

            for (attempt in 1..3) {
                try {
                    Log.d("GenAIRepository", "Attempt $attempt — calling Gemini API...")
                    val response = apiService.generateContent(request)
                    Log.d("GenAIRepository", "API call succeeded! candidates=${response.candidates?.size}")
                    val aiText = response.candidates?.firstOrNull()
                        ?.content?.parts?.firstOrNull()?.text
                        ?: getDefaultResponse(language)

                    val conversation = GenAIConversation(
                        babyId = babyId,
                        timestamp = System.currentTimeMillis(),
                        userQuery = query,
                        aiResponse = aiText,
                        language = language,
                        isFromCache = false
                    )
                    val id = dao.insert(conversation.toEntity())
                    return conversation.copy(id = id)
                } catch (e: retrofit2.HttpException) {
                    val errorBody = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
                    Log.e("GenAIRepository", "HTTP ${e.code()}: $errorBody", e)
                    lastException = e
                    lastErrorBody = errorBody
                    if (e.code() == 429 && attempt < 3) {
                        val delayMs = (1L shl attempt) * 1000L
                        Log.w("GenAIRepository", "Rate limited (429), retry $attempt in ${delayMs}ms")
                        kotlinx.coroutines.delay(delayMs)
                    } else {
                        break
                    }
                } catch (e: Exception) {
                    Log.e("GenAIRepository", "Non-HTTP error: ${e.javaClass.simpleName}: ${e.message}", e)
                    lastException = e
                    break
                }
            }

            // All retries failed — return specific error with details
            val errorMsg = lastException?.let { "${it.javaClass.simpleName}: ${it.message}" } ?: "Unknown"
            Log.e("GenAIRepository", "All attempts failed: $errorMsg")

            if (lastException is retrofit2.HttpException) {
                val httpEx = lastException as retrofit2.HttpException
                val errorResponse = when (httpEx.code()) {
                    429 -> if (language == "hi")
                        "AI सर्वर व्यस्त है, कृपया कुछ सेकंड बाद फिर से कोशिश करें।"
                    else
                        "The AI server is busy right now. Please wait a few seconds and try again."
                    403 -> if (language == "hi")
                        "API कुंजी अमान्य है। कृपया सेटिंग्स में जांचें।"
                    else
                        "API key is invalid. Please check your settings."
                    400 -> "Bad request (400): ${lastErrorBody?.take(300) ?: "no details"}"
                    else -> "Server error (${httpEx.code()}): ${lastErrorBody?.take(200) ?: "no details"}"
                }
                val conversation = GenAIConversation(
                    babyId = babyId,
                    timestamp = System.currentTimeMillis(),
                    userQuery = query,
                    aiResponse = errorResponse,
                    language = language,
                    isFromCache = true
                )
                val id = dao.insert(conversation.toEntity())
                return conversation.copy(id = id)
            } else if (lastException != null) {
                // Show the actual error to help debug
                val debugResponse = "Error: ${lastException.javaClass.simpleName} — ${lastException.message ?: "no details"}. Please check your internet connection and try again."
                val conversation = GenAIConversation(
                    babyId = babyId,
                    timestamp = System.currentTimeMillis(),
                    userQuery = query,
                    aiResponse = debugResponse,
                    language = language,
                    isFromCache = true
                )
                val id = dao.insert(conversation.toEntity())
                return conversation.copy(id = id)
            }
        }

        // Fallback: generic helpful response
        val fallbackResponse = getDefaultResponse(language)
        val conversation = GenAIConversation(
            babyId = babyId,
            timestamp = System.currentTimeMillis(),
            userQuery = query,
            aiResponse = fallbackResponse,
            language = language,
            isFromCache = true
        )
        val id = dao.insert(conversation.toEntity())
        return conversation.copy(id = id)
    }

    private fun findCachedAnswer(query: String, language: String): String? {
        val queryLower = query.lowercase()
        val match = FeedingTips.mythBusters.find { myth ->
            val q = if (language == "hi") myth.questionHi.lowercase() else myth.questionEn.lowercase()
            queryLower.contains(q.take(20)) ||
                q.split(" ").count { word -> word.length > 3 && queryLower.contains(word) } >= 3
        }
        return match?.let { if (language == "hi") it.answerHi else it.answerEn }
    }

    private fun buildPrompt(query: String, language: String, ageWeeks: Int): String {
        val lang = if (language == "hi") "Hindi" else "English"
        return """You are Shishu-Sneh, a caring AI health guide for new mothers in India.
            |The baby is $ageWeeks weeks old.
            |Answer the following question in $lang in a warm, supportive tone.
            |Keep your answer under 150 words. Focus on practical advice.
            |IMPORTANT: You are NOT a doctor. Always end with a reminder to consult a healthcare provider.
            |
            |Mother's question: $query""".trimMargin()
    }

    private fun getDefaultResponse(language: String): String {
        return if (language == "hi") {
            "मुझे अभी इंटरनेट से जुड़ने में दिक्कत हो रही है। कृपया अपने नज़दीकी स्वास्थ्य केंद्र या डॉक्टर से संपर्क करें। आप 'सामान्य सवाल' सेक्शन में ऑफलाइन जवाब भी देख सकती हैं।"
        } else {
            "I'm having trouble connecting to the internet right now. Please consult your nearest health centre or doctor. You can also check the 'Common Questions' section for offline answers."
        }
    }

    private fun GenAIConversationEntity.toDomain() = GenAIConversation(
        id = id, babyId = babyId, timestamp = timestamp,
        userQuery = userQuery, aiResponse = aiResponse,
        language = language, isFromCache = isFromCache,
        feedbackRating = feedbackRating
    )

    private fun GenAIConversation.toEntity() = GenAIConversationEntity(
        id = id, babyId = babyId, timestamp = timestamp,
        userQuery = userQuery, aiResponse = aiResponse,
        language = language, isFromCache = isFromCache,
        feedbackRating = feedbackRating
    )
}
