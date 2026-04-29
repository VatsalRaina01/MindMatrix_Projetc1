package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Feeding log entity — tracks breastfeeding, formula, and solid food sessions.
 * Used for feeding frequency analysis and reminder scheduling.
 */
@Entity(
    tableName = "feeding_logs",
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
data class FeedingLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val timestamp: Long,             // When feeding started
    val feedType: String,            // BREAST, FORMULA, SOLID
    val durationMins: Int? = null,   // Duration in minutes (for breastfeeding)
    val quantityMl: Int? = null,     // Quantity in ml (for formula)
    val foodDescription: String? = null, // Description for solid foods
    val side: String? = null,        // LEFT, RIGHT, BOTH (for breastfeeding)
    val notes: String? = null
)
