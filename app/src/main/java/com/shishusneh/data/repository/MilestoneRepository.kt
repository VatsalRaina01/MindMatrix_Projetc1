package com.shishusneh.data.repository

import com.shishusneh.data.constants.MilestoneData
import com.shishusneh.data.local.dao.MilestoneDao
import com.shishusneh.data.local.entity.MilestoneEntity
import com.shishusneh.domain.model.Milestone
import com.shishusneh.domain.model.MilestoneDomain
import com.shishusneh.domain.model.MilestoneStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneRepository @Inject constructor(
    private val dao: MilestoneDao
) {
    fun getMilestonesByWeek(babyId: Long, week: Int): Flow<List<Milestone>> =
        dao.getMilestonesByWeek(babyId, week).map { list -> list.map { it.toDomain() } }

    fun getAllMilestones(babyId: Long): Flow<List<Milestone>> =
        dao.getAllMilestones(babyId).map { list -> list.map { it.toDomain() } }

    fun getAvailableWeeks(babyId: Long): Flow<List<Int>> =
        dao.getAvailableWeeks(babyId)

    suspend fun updateStatus(id: Long, status: MilestoneStatus) {
        val achievedDate = if (status == MilestoneStatus.ACHIEVED) System.currentTimeMillis() else null
        dao.updateStatus(id, status.name, achievedDate)
    }

    suspend fun updatePhoto(id: Long, photoUri: String) =
        dao.updatePhoto(id, photoUri)

    suspend fun getNotYetCount(babyId: Long, fromWeek: Int): Int =
        dao.getNotYetCountFromWeek(babyId, fromWeek)

    suspend fun getAchievedCount(babyId: Long): Int = dao.getAchievedCount(babyId)
    suspend fun getTotalCount(babyId: Long): Int = dao.getTotalCount(babyId)

    /**
     * Generates all milestone records for a baby.
     * Pre-populates from MilestoneData constants.
     */
    suspend fun generateMilestones(babyId: Long) {
        val milestones = MilestoneData.milestones.map { template ->
            MilestoneEntity(
                babyId = babyId,
                weekNumber = template.weekNumber,
                title = template.title,
                titleHi = template.titleHi,
                description = template.description,
                descriptionHi = template.descriptionHi,
                domain = template.domain
            )
        }
        dao.insertAll(milestones)
    }

    private fun MilestoneEntity.toDomain() = Milestone(
        id = id, babyId = babyId, weekNumber = weekNumber,
        title = title, titleHi = titleHi,
        description = description, descriptionHi = descriptionHi,
        domain = MilestoneDomain.valueOf(domain),
        status = MilestoneStatus.valueOf(status),
        achievedDate = achievedDate, photoUri = photoUri
    )
}
