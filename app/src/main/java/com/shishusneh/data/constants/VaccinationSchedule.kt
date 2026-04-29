package com.shishusneh.data.constants

/**
 * India's Universal Immunisation Programme (UIP) vaccine schedule.
 * Source: Ministry of Health and Family Welfare, Government of India (2024).
 *
 * Each vaccine entry defines:
 * - Name (EN/HI), disease prevented, dose info
 * - Week offset from DOB for target date calculation
 * - Whether it's conditional (e.g., JE in endemic areas only)
 */
object VaccinationSchedule {

    data class VaccineTemplate(
        val name: String,
        val nameHi: String,
        val disease: String,
        val diseaseHi: String,
        val doseNumber: Int,
        val totalDoses: Int,
        val weekOffset: Int,          // Weeks from DOB
        val ageLabel: String,
        val ageLabelHi: String,
        val isConditional: Boolean = false
    )

    /**
     * Complete Phase 1 vaccine schedule (0–12 months).
     * Ordered chronologically by week offset.
     */
    val schedule: List<VaccineTemplate> = listOf(
        // === BIRTH (Week 0) ===
        VaccineTemplate(
            name = "BCG",
            nameHi = "बीसीजी",
            disease = "Tuberculosis (TB)",
            diseaseHi = "क्षय रोग (टीबी)",
            doseNumber = 1, totalDoses = 1,
            weekOffset = 0,
            ageLabel = "At Birth",
            ageLabelHi = "जन्म के समय"
        ),
        VaccineTemplate(
            name = "OPV-0",
            nameHi = "ओपीवी-0 (पोलियो)",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 0, totalDoses = 5,
            weekOffset = 0,
            ageLabel = "At Birth",
            ageLabelHi = "जन्म के समय"
        ),
        VaccineTemplate(
            name = "Hepatitis B - Birth Dose",
            nameHi = "हेपेटाइटिस बी - जन्म खुराक",
            disease = "Hepatitis B",
            diseaseHi = "हेपेटाइटिस बी",
            doseNumber = 1, totalDoses = 4,
            weekOffset = 0,
            ageLabel = "At Birth",
            ageLabelHi = "जन्म के समय"
        ),

        // === 6 WEEKS ===
        VaccineTemplate(
            name = "OPV-1",
            nameHi = "ओपीवी-1 (पोलियो)",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 1, totalDoses = 5,
            weekOffset = 6,
            ageLabel = "6 Weeks",
            ageLabelHi = "6 सप्ताह"
        ),
        VaccineTemplate(
            name = "Pentavalent-1",
            nameHi = "पेंटावैलेंट-1",
            disease = "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib",
            diseaseHi = "डिप्थीरिया, काली खांसी, टेटनस, हेपेटाइटिस बी, हिब",
            doseNumber = 1, totalDoses = 3,
            weekOffset = 6,
            ageLabel = "6 Weeks",
            ageLabelHi = "6 सप्ताह"
        ),
        VaccineTemplate(
            name = "Rotavirus-1",
            nameHi = "रोटावायरस-1",
            disease = "Rotavirus Diarrhoea",
            diseaseHi = "रोटावायरस दस्त",
            doseNumber = 1, totalDoses = 3,
            weekOffset = 6,
            ageLabel = "6 Weeks",
            ageLabelHi = "6 सप्ताह"
        ),
        VaccineTemplate(
            name = "fIPV-1",
            nameHi = "एफआईपीवी-1",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 1, totalDoses = 2,
            weekOffset = 6,
            ageLabel = "6 Weeks",
            ageLabelHi = "6 सप्ताह"
        ),
        VaccineTemplate(
            name = "PCV-1",
            nameHi = "पीसीवी-1 (न्यूमोकोकल)",
            disease = "Pneumococcal Pneumonia",
            diseaseHi = "न्यूमोकोकल निमोनिया",
            doseNumber = 1, totalDoses = 3,
            weekOffset = 6,
            ageLabel = "6 Weeks",
            ageLabelHi = "6 सप्ताह"
        ),

        // === 10 WEEKS ===
        VaccineTemplate(
            name = "OPV-2",
            nameHi = "ओपीवी-2 (पोलियो)",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 2, totalDoses = 5,
            weekOffset = 10,
            ageLabel = "10 Weeks",
            ageLabelHi = "10 सप्ताह"
        ),
        VaccineTemplate(
            name = "Pentavalent-2",
            nameHi = "पेंटावैलेंट-2",
            disease = "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib",
            diseaseHi = "डिप्थीरिया, काली खांसी, टेटनस, हेपेटाइटिस बी, हिब",
            doseNumber = 2, totalDoses = 3,
            weekOffset = 10,
            ageLabel = "10 Weeks",
            ageLabelHi = "10 सप्ताह"
        ),
        VaccineTemplate(
            name = "Rotavirus-2",
            nameHi = "रोटावायरस-2",
            disease = "Rotavirus Diarrhoea",
            diseaseHi = "रोटावायरस दस्त",
            doseNumber = 2, totalDoses = 3,
            weekOffset = 10,
            ageLabel = "10 Weeks",
            ageLabelHi = "10 सप्ताह"
        ),

        // === 14 WEEKS ===
        VaccineTemplate(
            name = "OPV-3",
            nameHi = "ओपीवी-3 (पोलियो)",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 3, totalDoses = 5,
            weekOffset = 14,
            ageLabel = "14 Weeks",
            ageLabelHi = "14 सप्ताह"
        ),
        VaccineTemplate(
            name = "Pentavalent-3",
            nameHi = "पेंटावैलेंट-3",
            disease = "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib",
            diseaseHi = "डिप्थीरिया, काली खांसी, टेटनस, हेपेटाइटिस बी, हिब",
            doseNumber = 3, totalDoses = 3,
            weekOffset = 14,
            ageLabel = "14 Weeks",
            ageLabelHi = "14 सप्ताह"
        ),
        VaccineTemplate(
            name = "Rotavirus-3",
            nameHi = "रोटावायरस-3",
            disease = "Rotavirus Diarrhoea",
            diseaseHi = "रोटावायरस दस्त",
            doseNumber = 3, totalDoses = 3,
            weekOffset = 14,
            ageLabel = "14 Weeks",
            ageLabelHi = "14 सप्ताह"
        ),
        VaccineTemplate(
            name = "fIPV-2",
            nameHi = "एफआईपीवी-2",
            disease = "Poliomyelitis (Polio)",
            diseaseHi = "पोलियो",
            doseNumber = 2, totalDoses = 2,
            weekOffset = 14,
            ageLabel = "14 Weeks",
            ageLabelHi = "14 सप्ताह"
        ),
        VaccineTemplate(
            name = "PCV-2",
            nameHi = "पीसीवी-2 (न्यूमोकोकल)",
            disease = "Pneumococcal Pneumonia",
            diseaseHi = "न्यूमोकोकल निमोनिया",
            doseNumber = 2, totalDoses = 3,
            weekOffset = 14,
            ageLabel = "14 Weeks",
            ageLabelHi = "14 सप्ताह"
        ),

        // === 9 MONTHS (39 Weeks) ===
        VaccineTemplate(
            name = "Measles-Rubella (MR-1)",
            nameHi = "खसरा-रूबेला (एमआर-1)",
            disease = "Measles, Rubella",
            diseaseHi = "खसरा, रूबेला",
            doseNumber = 1, totalDoses = 2,
            weekOffset = 39,
            ageLabel = "9 Months",
            ageLabelHi = "9 महीने"
        ),
        VaccineTemplate(
            name = "JE-1 (Japanese Encephalitis)",
            nameHi = "जेई-1 (जापानी इन्सेफेलाइटिस)",
            disease = "Japanese Encephalitis",
            diseaseHi = "जापानी इन्सेफेलाइटिस",
            doseNumber = 1, totalDoses = 2,
            weekOffset = 39,
            ageLabel = "9 Months",
            ageLabelHi = "9 महीने",
            isConditional = true  // Only in endemic areas
        ),
        VaccineTemplate(
            name = "Vitamin A - 1st Dose",
            nameHi = "विटामिन ए - पहली खुराक",
            disease = "Vitamin A Deficiency",
            diseaseHi = "विटामिन ए की कमी",
            doseNumber = 1, totalDoses = 2,
            weekOffset = 39,
            ageLabel = "9 Months",
            ageLabelHi = "9 महीने"
        ),

        // === 12 MONTHS (52 Weeks) ===
        VaccineTemplate(
            name = "PCV Booster",
            nameHi = "पीसीवी बूस्टर",
            disease = "Pneumococcal Pneumonia (Booster)",
            diseaseHi = "न्यूमोकोकल निमोनिया (बूस्टर)",
            doseNumber = 3, totalDoses = 3,
            weekOffset = 52,
            ageLabel = "12 Months",
            ageLabelHi = "12 महीने"
        )
    )

    /**
     * Returns the total number of vaccines in the schedule.
     * Excludes conditional vaccines (JE) by default.
     */
    fun getTotalVaccineCount(includeConditional: Boolean = false): Int {
        return if (includeConditional) {
            schedule.size
        } else {
            schedule.count { !it.isConditional }
        }
    }

    /**
     * Gets vaccines grouped by age label for timeline display.
     */
    fun getGroupedByAge(): Map<String, List<VaccineTemplate>> {
        return schedule.groupBy { it.ageLabel }
    }
}
