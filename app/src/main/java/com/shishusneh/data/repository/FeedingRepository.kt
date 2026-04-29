package com.shishusneh.data.repository

import com.shishusneh.data.local.dao.FeedingLogDao
import com.shishusneh.data.local.entity.FeedingLogEntity
import com.shishusneh.domain.model.BreastSide
import com.shishusneh.domain.model.FeedType
import com.shishusneh.domain.model.FeedingLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedingRepository @Inject constructor(
    private val dao: FeedingLogDao
) {
    fun getAllLogs(babyId: Long): Flow<List<FeedingLog>> =
        dao.getAllLogs(babyId).map { list -> list.map { it.toDomain() } }

    fun getRecentLogs(babyId: Long, limit: Int = 10): Flow<List<FeedingLog>> =
        dao.getRecentLogs(babyId, limit).map { list -> list.map { it.toDomain() } }

    fun getLastFeeding(babyId: Long): Flow<FeedingLog?> =
        dao.getLastFeeding(babyId).map { it?.toDomain() }

    suspend fun addLog(log: FeedingLog): Long =
        dao.insert(log.toEntity())

    suspend fun deleteLog(log: FeedingLog) =
        dao.delete(log.toEntity())

    suspend fun getFeedingCountToday(babyId: Long): Int {
        val now = System.currentTimeMillis()
        val startOfDay = now - (now % (24 * 60 * 60 * 1000))
        return dao.getFeedingCountInRange(babyId, startOfDay, now)
    }

    private fun FeedingLogEntity.toDomain() = FeedingLog(
        id = id, babyId = babyId, timestamp = timestamp,
        feedType = FeedType.valueOf(feedType),
        durationMins = durationMins, quantityMl = quantityMl,
        foodDescription = foodDescription,
        side = side?.let { BreastSide.valueOf(it) },
        notes = notes
    )

    private fun FeedingLog.toEntity() = FeedingLogEntity(
        id = id, babyId = babyId, timestamp = timestamp,
        feedType = feedType.name,
        durationMins = durationMins, quantityMl = quantityMl,
        foodDescription = foodDescription,
        side = side?.name, notes = notes
    )
}
