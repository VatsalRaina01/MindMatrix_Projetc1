package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Baby profile entity — core data for the child being tracked.
 * Single profile in Phase 1; multi-child support planned for Phase 2.
 */
@Entity(tableName = "baby_profiles")
data class BabyProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val motherName: String,
    val dateOfBirth: Long,           // Epoch millis (UTC)
    val birthWeightKg: Float,
    val gender: String,              // "MALE" or "FEMALE"
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
