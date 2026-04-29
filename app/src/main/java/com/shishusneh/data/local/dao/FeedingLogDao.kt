package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.data.local.entity.FeedingLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for feeding log operations.
 * Supports time-range queries for feeding pattern analysis.
 */
@Dao
interface FeedingLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: FeedingLogEntity): Long

    @Delete
    suspend fun delete(log: FeedingLogEntity)

    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId ORDER BY timestamp DESC")
    fun getAllLogs(babyId: Long): Flow<List<FeedingLogEntity>>

    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId AND timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getLogsByDateRange(babyId: Long, startTime: Long, endTime: Long): Flow<List<FeedingLogEntity>>

    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId ORDER BY timestamp DESC LIMIT 1")
    fun getLastFeeding(babyId: Long): Flow<FeedingLogEntity?>

    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(babyId: Long, limit: Int): Flow<List<FeedingLogEntity>>

    @Query("SELECT COUNT(*) FROM feeding_logs WHERE babyId = :babyId AND timestamp BETWEEN :startTime AND :endTime")
    suspend fun getFeedingCountInRange(babyId: Long, startTime: Long, endTime: Long): Int

    @Query("""
        SELECT AVG(durationMins) FROM feeding_logs 
        WHERE babyId = :babyId 
        AND feedType = 'BREAST' 
        AND durationMins IS NOT NULL 
        AND timestamp BETWEEN :startTime AND :endTime
    """)
    suspend fun getAverageBreastfeedingDuration(babyId: Long, startTime: Long, endTime: Long): Float?

    @Query("DELETE FROM feeding_logs WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: Long)
}
