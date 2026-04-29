package com.shishusneh.data.repository

import com.shishusneh.data.constants.VaccinationSchedule
import com.shishusneh.data.local.dao.VaccinationDao
import com.shishusneh.data.local.entity.VaccinationEntity
import com.shishusneh.domain.model.Vaccination
import com.shishusneh.domain.model.VaccinationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccinationRepository @Inject constructor(
    private val dao: VaccinationDao
) {
    fun getAllVaccinations(babyId: Long): Flow<List<Vaccination>> =
        dao.getAllVaccinations(babyId).map { list -> list.map { it.toDomain() } }

    fun getPendingVaccinations(babyId: Long): Flow<List<Vaccination>> =
        dao.getPendingVaccinations(babyId).map { list -> list.map { it.toDomain() } }

    fun getNextUpcoming(babyId: Long): Flow<Vaccination?> =
        dao.getNextUpcomingVaccination(babyId).map { it?.toDomain() }

    fun getOverdueVaccinations(babyId: Long): Flow<List<Vaccination>> =
        dao.getOverdueVaccinations(babyId).map { list -> list.map { it.toDomain() } }

    suspend fun markAsDone(id: Long, administeredDate: Long) =
        dao.markAsDone(id, administeredDate)

    suspend fun markAsPending(id: Long) = dao.markAsPending(id)

    suspend fun updateOverdueStatus(babyId: Long, currentDate: Long) =
        dao.updateOverdueStatus(babyId, currentDate)

    suspend fun getCompletedCount(babyId: Long): Int = dao.getCompletedCount(babyId)
    suspend fun getTotalCount(babyId: Long): Int = dao.getTotalCount(babyId)

    /**
     * Generates the complete vaccination schedule from baby's DOB.
     * Inserts all vaccine records into the database.
     */
    suspend fun generateSchedule(babyId: Long, dobMillis: Long) {
        val vaccinations = VaccinationSchedule.schedule.map { template ->
            val targetDate = dobMillis + (template.weekOffset * 7L * 24 * 60 * 60 * 1000)
            val currentTime = System.currentTimeMillis()
            val status = if (targetDate < currentTime) "OVERDUE" else "PENDING"

            VaccinationEntity(
                babyId = babyId,
                vaccineName = template.name,
                vaccineNameHi = template.nameHi,
                targetDate = targetDate,
                status = status,
                disease = template.disease,
                diseaseHi = template.diseaseHi,
                doseNumber = template.doseNumber,
                totalDoses = template.totalDoses,
                ageLabel = template.ageLabel,
                ageLabelHi = template.ageLabelHi,
                isConditional = template.isConditional
            )
        }
        dao.insertAll(vaccinations)
    }

    suspend fun getUpcomingInRange(babyId: Long, startDate: Long, endDate: Long): List<Vaccination> =
        dao.getUpcomingInRange(babyId, startDate, endDate).map { it.toDomain() }

    private fun VaccinationEntity.toDomain() = Vaccination(
        id = id, babyId = babyId, vaccineName = vaccineName,
        vaccineNameHi = vaccineNameHi, targetDate = targetDate,
        administeredDate = administeredDate,
        status = VaccinationStatus.valueOf(status),
        disease = disease, diseaseHi = diseaseHi,
        doseNumber = doseNumber, totalDoses = totalDoses,
        ageLabel = ageLabel, ageLabelHi = ageLabelHi,
        isConditional = isConditional
    )
}
