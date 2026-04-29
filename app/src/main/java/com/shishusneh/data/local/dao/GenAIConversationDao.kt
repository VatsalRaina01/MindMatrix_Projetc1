package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.data.local.entity.GenAIConversationEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for GenAI conversation history.
 * Stores all Q&A pairs for offline access and feedback tracking.
 */
@Dao
interface GenAIConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversation: GenAIConversationEntity): Long

    @Update
    suspend fun update(conversation: GenAIConversationEntity)

    @Query("SELECT * FROM genai_conversations WHERE babyId = :babyId ORDER BY timestamp DESC")
    fun getAllConversations(babyId: Long): Flow<List<GenAIConversationEntity>>

    @Query("SELECT * FROM genai_conversations WHERE babyId = :babyId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentConversations(babyId: Long, limit: Int): Flow<List<GenAIConversationEntity>>

    @Query("UPDATE genai_conversations SET feedbackRating = :rating WHERE id = :id")
    suspend fun updateFeedback(id: Long, rating: Int)

    @Query("SELECT COUNT(*) FROM genai_conversations WHERE babyId = :babyId AND feedbackRating = 2")
    suspend fun getPositiveFeedbackCount(babyId: Long): Int

    @Query("SELECT COUNT(*) FROM genai_conversations WHERE babyId = :babyId")
    suspend fun getTotalConversationCount(babyId: Long): Int

    @Query("DELETE FROM genai_conversations WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: Long)
}
