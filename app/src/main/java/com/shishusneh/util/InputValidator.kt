package com.shishusneh.util

/**
 * Input validation for all user-facing forms.
 * Prevents invalid/dangerous data from entering the system.
 */
object InputValidator {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessageEn: String? = null,
        val errorMessageHi: String? = null
    )

    fun validateBabyName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Please enter baby's name", "कृपया बच्चे का नाम दर्ज करें")
            trimmed.length < 2 -> ValidationResult(false, "Name must be at least 2 characters", "नाम कम से कम 2 अक्षर का होना चाहिए")
            trimmed.length > 50 -> ValidationResult(false, "Name must be under 50 characters", "नाम 50 अक्षर से कम होना चाहिए")
            !trimmed.matches(Regex("^[\\p{L}\\s'.\\-]+$")) -> ValidationResult(false, "Name contains invalid characters", "नाम में अमान्य अक्षर हैं")
            else -> ValidationResult(true)
        }
    }

    fun validateMotherName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Please enter mother's name", "कृपया माँ का नाम दर्ज करें")
            trimmed.length > 50 -> ValidationResult(false, "Name must be under 50 characters", "नाम 50 अक्षर से कम होना चाहिए")
            else -> ValidationResult(true)
        }
    }

    fun validateDOB(dobMillis: Long): ValidationResult {
        val now = System.currentTimeMillis()
        val twoYearsAgo = now - (2L * 365 * 24 * 60 * 60 * 1000)
        return when {
            dobMillis > now -> ValidationResult(false, "Date of birth cannot be in the future", "जन्म तिथि भविष्य में नहीं हो सकती")
            dobMillis < twoYearsAgo -> ValidationResult(false, "This app is designed for babies 0–12 months", "यह ऐप 0–12 महीने के बच्चों के लिए है")
            else -> ValidationResult(true)
        }
    }

    fun validateWeight(weightKg: Float): ValidationResult {
        return when {
            weightKg <= 0f -> ValidationResult(false, "Weight must be greater than 0", "वज़न 0 से ज़्यादा होना चाहिए")
            weightKg < 0.3f -> ValidationResult(false, "Weight seems too low. Please verify.", "वज़न बहुत कम लग रहा है। कृपया जाँचें।")
            weightKg > 25f -> ValidationResult(false, "Weight seems too high for a baby. Please verify.", "बच्चे के लिए वज़न बहुत ज़्यादा है। कृपया जाँचें।")
            else -> ValidationResult(true)
        }
    }

    fun validateBirthWeight(weightKg: Float): ValidationResult {
        return when {
            weightKg <= 0f -> ValidationResult(false, "Birth weight must be greater than 0", "जन्म का वज़न 0 से ज़्यादा होना चाहिए")
            weightKg < 0.5f -> ValidationResult(false, "Very low birth weight — please verify", "बहुत कम जन्म वज़न — कृपया जाँचें")
            weightKg > 6.0f -> ValidationResult(false, "Birth weight seems too high. Please verify.", "जन्म का वज़न बहुत ज़्यादा है। कृपया जाँचें।")
            else -> ValidationResult(true)
        }
    }

    fun validateHeight(heightCm: Float): ValidationResult {
        return when {
            heightCm <= 0f -> ValidationResult(false, "Height must be greater than 0", "ऊंचाई 0 से ज़्यादा होनी चाहिए")
            heightCm < 20f -> ValidationResult(false, "Height seems too low. Please verify.", "ऊंचाई बहुत कम लग रही है। कृपया जाँचें।")
            heightCm > 120f -> ValidationResult(false, "Height seems too high. Please verify.", "ऊंचाई बहुत ज़्यादा लग रही है। कृपया जाँचें।")
            else -> ValidationResult(true)
        }
    }

    fun validateMeasurementDate(dateMillis: Long, dobMillis: Long): ValidationResult {
        val now = System.currentTimeMillis()
        return when {
            dateMillis > now -> ValidationResult(false, "Measurement date cannot be in the future", "माप की तिथि भविष्य में नहीं हो सकती")
            dateMillis < dobMillis -> ValidationResult(false, "Measurement date cannot be before birth", "माप की तिथि जन्म से पहले नहीं हो सकती")
            else -> ValidationResult(true)
        }
    }

    /**
     * Checks if a weight change between two consecutive entries is suspicious.
     * A baby gaining/losing more than 2kg in a month is flagged.
     */
    fun checkWeightChange(previousWeight: Float, newWeight: Float, daysBetween: Int): ValidationResult {
        if (daysBetween <= 0) return ValidationResult(true)
        val change = kotlin.math.abs(newWeight - previousWeight)
        val monthlyChange = change * (30f / daysBetween)
        return when {
            monthlyChange > 2.0f && newWeight > previousWeight ->
                ValidationResult(false, "Large weight gain detected (${String.format("%.1f", change)}kg). Please verify.", "बड़ा वज़न बढ़ा (${String.format("%.1f", change)} किग्रा)। कृपया जाँचें।")
            newWeight < previousWeight * 0.85f ->
                ValidationResult(false, "Significant weight loss detected. Please verify.", "वज़न में बड़ी कमी। कृपया जाँचें।")
            else -> ValidationResult(true)
        }
    }
}
