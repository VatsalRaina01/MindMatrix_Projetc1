package com.shishusneh.data.constants

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Vaccination Schedule.
 */
class VaccinationScheduleTest {

    @Test
    fun `schedule has correct number of vaccines`() {
        // 21 total vaccines including JE (conditional)
        assertEquals(21, VaccinationSchedule.schedule.size)
    }

    @Test
    fun `birth vaccines have week offset 0`() {
        val birthVaccines = VaccinationSchedule.schedule.filter { it.weekOffset == 0 }
        assertEquals(3, birthVaccines.size) // BCG, OPV-0, Hep-B
    }

    @Test
    fun `6 week vaccines have correct offset`() {
        val sixWeekVaccines = VaccinationSchedule.schedule.filter { it.weekOffset == 6 }
        assertEquals(5, sixWeekVaccines.size) // OPV-1, Penta-1, Rota-1, fIPV-1, PCV-1
    }

    @Test
    fun `JE vaccine is marked conditional`() {
        val je = VaccinationSchedule.schedule.find { it.name.contains("JE") }
        assertNotNull(je)
        assertTrue(je!!.isConditional)
    }

    @Test
    fun `non-conditional count excludes JE`() {
        val nonConditional = VaccinationSchedule.getTotalVaccineCount(false)
        val withConditional = VaccinationSchedule.getTotalVaccineCount(true)
        assertEquals(withConditional - 1, nonConditional) // Only JE is conditional
    }

    @Test
    fun `all vaccines have Hindi names`() {
        VaccinationSchedule.schedule.forEach { vaccine ->
            assertTrue("${vaccine.name} missing Hindi name", vaccine.nameHi.isNotBlank())
            assertTrue("${vaccine.name} missing Hindi disease", vaccine.diseaseHi.isNotBlank())
        }
    }

    @Test
    fun `vaccines are chronologically ordered`() {
        val offsets = VaccinationSchedule.schedule.map { it.weekOffset }
        assertEquals(offsets, offsets.sorted()) // Should be in ascending order
    }

    @Test
    fun `grouped by age produces correct groups`() {
        val grouped = VaccinationSchedule.getGroupedByAge()
        assertTrue(grouped.containsKey("At Birth"))
        assertTrue(grouped.containsKey("6 Weeks"))
        assertTrue(grouped.containsKey("9 Months"))
        assertTrue(grouped.containsKey("12 Months"))
    }
}
