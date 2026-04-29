package com.shishusneh.data.remote

import com.shishusneh.data.remote.dto.GeminiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service for Google Gemini API.
 */
interface GeminiApiService {

    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Body request: GeminiRequest
    ): GeminiResponse
}

// Flat (non-nested) data classes for reliable Moshi serialization
@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String
)

/**
 * Helper to build a GeminiRequest from a simple prompt string.
 */
fun buildGeminiRequest(prompt: String): GeminiRequest {
    return GeminiRequest(
        contents = listOf(
            GeminiContent(
                parts = listOf(GeminiPart(text = prompt))
            )
        )
    )
}
