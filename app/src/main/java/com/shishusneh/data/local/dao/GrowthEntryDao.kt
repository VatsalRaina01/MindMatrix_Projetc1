package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.data.local.entity.GrowthEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for growth entry operations.
 * Supports chronological listing, latest entry retrieval, and date-range queries.
 */
@Dao
interface GrowthEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: GrowthEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: GrowthEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: GrowthEntryEntity)

    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date ASC")
    fun getAllEntries(babyId: Long): Flow<List<GrowthEntryEntity>>

    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date DESC LIMIT 1")
    fun getLatestEntry(babyId: Long): Flow<GrowthEntryEntity?>

    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntryOnce(babyId: Long): GrowthEntryEntity?

    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEntriesByDateRange(babyId: Long, startDate: Long, endDate: Long): Flow<List<GrowthEntryEntity>>

    @Query("SELECT COUNT(*) FROM growth_entries WHERE babyId = :babyId")
    suspend fun getEntryCount(babyId: Long): Int

    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId AND date = :date LIMIT 1")
    suspend fun getEntryByDate(babyId: Long, date: Long): GrowthEntryEntity?

    @Query("DELETE FROM growth_entries WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: Long)
}
