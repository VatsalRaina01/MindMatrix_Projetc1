package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Milestone entity — weekly developmental milestone checklist.
 * Covers 4 WHO domains: Motor, Language, Social, Cognitive.
 * Advisory triggered if 3+ consecutive milestones are NOT_YET.
 */
@Entity(
    tableName = "milestones",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["babyId"]), Index(value = ["weekNumber"])]
)
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val weekNumber: Int,             // 0–52
    val title: String,               // English title
    val titleHi: String,             // Hindi title
    val description: String = "",    // Detailed description
    val descriptionHi: String = "",  // Hindi description
    val domain: String,              // MOTOR, LANGUAGE, SOCIAL, COGNITIVE
    val status: String = "NOT_YET",  // ACHIEVED, NOT_YET, SKIPPED
    val achievedDate: Long? = null,
    val photoUri: String? = null     // Advancement: photo milestone capture
)
