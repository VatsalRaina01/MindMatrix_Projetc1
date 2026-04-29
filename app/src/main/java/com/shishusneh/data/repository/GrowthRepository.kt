package com.shishusneh.data.repository

import com.shishusneh.data.local.dao.GrowthEntryDao
import com.shishusneh.data.local.entity.GrowthEntryEntity
import com.shishusneh.domain.model.GrowthEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrowthRepository @Inject constructor(
    private val dao: GrowthEntryDao
) {
    fun getAllEntries(babyId: Long): Flow<List<GrowthEntry>> =
        dao.getAllEntries(babyId).map { list -> list.map { it.toDomain() } }

    fun getLatestEntry(babyId: Long): Flow<GrowthEntry?> =
        dao.getLatestEntry(babyId).map { it?.toDomain() }

    suspend fun addEntry(entry: GrowthEntry): Long =
        dao.insertEntry(entry.toEntity())

    suspend fun updateEntry(entry: GrowthEntry) =
        dao.updateEntry(entry.toEntity())

    suspend fun deleteEntry(entry: GrowthEntry) =
        dao.deleteEntry(entry.toEntity())

    suspend fun getEntryCount(babyId: Long): Int =
        dao.getEntryCount(babyId)

    suspend fun getEntryByDate(babyId: Long, date: Long): GrowthEntry? =
        dao.getEntryByDate(babyId, date)?.toDomain()

    private fun GrowthEntryEntity.toDomain() = GrowthEntry(
        id = id, babyId = babyId, date = date,
        weightKg = weightKg, heightCm = heightCm,
        headCircumferenceCm = headCircumferenceCm, notes = notes
    )

    private fun GrowthEntry.toEntity() = GrowthEntryEntity(
        id = id, babyId = babyId, date = date,
        weightKg = weightKg, heightCm = heightCm,
        headCircumferenceCm = headCircumferenceCm, notes = notes
    )
}
