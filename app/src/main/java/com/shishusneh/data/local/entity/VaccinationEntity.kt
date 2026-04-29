package com.shishusneh.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Vaccination entity — represents a single vaccine dose in the schedule.
 * Auto-generated from DOB per India's UIP schedule.
 * Status: PENDING → DONE or OVERDUE (if past target date and not administered).
 */
@Entity(
    tableName = "vaccinations",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["babyId"]), Index(value = ["targetDate"]), Index(value = ["status"])]
)
data class VaccinationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val vaccineName: String,
    val vaccineNameHi: String,       // Hindi name for i18n
    val targetDate: Long,            // Calculated from DOB + offset
    val administeredDate: Long? = null,
    val status: String = "PENDING",  // PENDING, DONE, OVERDUE
    val disease: String,             // Disease prevented (English)
    val diseaseHi: String,           // Disease prevented (Hindi)
    val doseNumber: Int,
    val totalDoses: Int,
    val ageLabel: String,            // e.g., "6 Weeks", "9 Months"
    val ageLabelHi: String,          // Hindi age label
    val isConditional: Boolean = false,  // e.g., JE only in endemic areas
    val notes: String? = null
)
