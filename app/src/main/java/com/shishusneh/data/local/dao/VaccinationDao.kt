package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.data.local.entity.VaccinationEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for vaccination operations.
 * Supports status filtering, overdue detection, and batch insertion.
 */
@Dao
interface VaccinationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vaccinations: List<VaccinationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vaccination: VaccinationEntity): Long

    @Update
    suspend fun update(vaccination: VaccinationEntity)

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId ORDER BY targetDate ASC")
    fun getAllVaccinations(babyId: Long): Flow<List<VaccinationEntity>>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND status = 'PENDING' ORDER BY targetDate ASC")
    fun getPendingVaccinations(babyId: Long): Flow<List<VaccinationEntity>>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND status = 'PENDING' ORDER BY targetDate ASC LIMIT 1")
    fun getNextUpcomingVaccination(babyId: Long): Flow<VaccinationEntity?>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND status = 'OVERDUE' ORDER BY targetDate ASC")
    fun getOverdueVaccinations(babyId: Long): Flow<List<VaccinationEntity>>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND status = 'DONE' ORDER BY administeredDate DESC")
    fun getCompletedVaccinations(babyId: Long): Flow<List<VaccinationEntity>>

    @Query("UPDATE vaccinations SET status = 'DONE', administeredDate = :administeredDate WHERE id = :id")
    suspend fun markAsDone(id: Long, administeredDate: Long)

    @Query("UPDATE vaccinations SET status = 'PENDING', administeredDate = NULL WHERE id = :id")
    suspend fun markAsPending(id: Long)

    @Query("UPDATE vaccinations SET status = 'OVERDUE' WHERE babyId = :babyId AND status = 'PENDING' AND targetDate < :currentDate")
    suspend fun updateOverdueStatus(babyId: Long, currentDate: Long)

    @Query("SELECT COUNT(*) FROM vaccinations WHERE babyId = :babyId AND status = 'DONE'")
    suspend fun getCompletedCount(babyId: Long): Int

    @Query("SELECT COUNT(*) FROM vaccinations WHERE babyId = :babyId")
    suspend fun getTotalCount(babyId: Long): Int

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND status = 'PENDING' AND targetDate BETWEEN :startDate AND :endDate ORDER BY targetDate ASC")
    suspend fun getUpcomingInRange(babyId: Long, startDate: Long, endDate: Long): List<VaccinationEntity>

    @Query("DELETE FROM vaccinations WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: Long)
}
