package com.shishusneh.domain.model

/**
 * Domain model for baby profile.
 */
data class BabyProfile(
    val id: Long = 0,
    val name: String,
    val motherName: String,
    val dateOfBirth: Long,
    val birthWeightKg: Float,
    val gender: Gender,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Gender { MALE, FEMALE }

data class GrowthEntry(
    val id: Long = 0,
    val babyId: Long,
    val date: Long,
    val weightKg: Float,
    val heightCm: Float? = null,
    val headCircumferenceCm: Float? = null,
    val notes: String? = null,
    val percentile: Double? = null,
    val growthZone: String? = null
)

enum class VaccinationStatus { PENDING, DONE, OVERDUE }

data class Vaccination(
    val id: Long = 0,
    val babyId: Long,
    val vaccineName: String,
    val vaccineNameHi: String,
    val targetDate: Long,
    val administeredDate: Long? = null,
    val status: VaccinationStatus = VaccinationStatus.PENDING,
    val disease: String,
    val diseaseHi: String,
    val doseNumber: Int,
    val totalDoses: Int,
    val ageLabel: String,
    val ageLabelHi: String,
    val isConditional: Boolean = false
)

enum class MilestoneStatus { ACHIEVED, NOT_YET, SKIPPED }
enum class MilestoneDomain { MOTOR, LANGUAGE, SOCIAL, COGNITIVE }

data class Milestone(
    val id: Long = 0,
    val babyId: Long,
    val weekNumber: Int,
    val title: String,
    val titleHi: String,
    val description: String = "",
    val descriptionHi: String = "",
    val domain: MilestoneDomain,
    val status: MilestoneStatus = MilestoneStatus.NOT_YET,
    val achievedDate: Long? = null,
    val photoUri: String? = null
)

enum class FeedType { BREAST, FORMULA, SOLID }
enum class BreastSide { LEFT, RIGHT, BOTH }

data class FeedingLog(
    val id: Long = 0,
    val babyId: Long,
    val timestamp: Long,
    val feedType: FeedType,
    val durationMins: Int? = null,
    val quantityMl: Int? = null,
    val foodDescription: String? = null,
    val side: BreastSide? = null,
    val notes: String? = null
)

data class GenAIConversation(
    val id: Long = 0,
    val babyId: Long,
    val timestamp: Long,
    val userQuery: String,
    val aiResponse: String,
    val language: String,
    val isFromCache: Boolean = false,
    val feedbackRating: Int? = null
)
