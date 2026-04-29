package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * GenAI conversation entity — stores chat history between mother and AI assistant.
 * Each entry is a single Q&A pair. All AI responses include a disclaimer.
 */
@Entity(
    tableName = "genai_conversations",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["babyId"]), Index(value = ["timestamp"])]
)
data class GenAIConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val timestamp: Long,
    val userQuery: String,
    val aiResponse: String,
    val language: String,            // "en" or "hi"
    val isFromCache: Boolean = false, // Whether response came from offline cache
    val feedbackRating: Int? = null  // 1 = thumbs down, 2 = thumbs up (null = no feedback)
)
