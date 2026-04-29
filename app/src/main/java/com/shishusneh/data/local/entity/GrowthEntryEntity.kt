package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Growth entry entity — records baby's weight and height at a point in time.
 * Linked to baby profile via foreign key with cascade delete.
 */
@Entity(
    tableName = "growth_entries",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["babyId"]), Index(value = ["date"])]
)
data class GrowthEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val date: Long,                  // Epoch millis (UTC) — date of measurement
    val weightKg: Float,
    val heightCm: Float? = null,     // Optional in Phase 1
    val headCircumferenceCm: Float? = null,  // Advancement: head circumference tracking
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
