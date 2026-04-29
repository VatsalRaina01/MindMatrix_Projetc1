package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.data.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for milestone operations.
 * Supports week-based queries, domain filtering, and consecutive "Not Yet" detection.
 */
@Dao
interface MilestoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(milestones: List<MilestoneEntity>)

    @Update
    suspend fun update(milestone: MilestoneEntity)

    @Query("SELECT * FROM milestones WHERE babyId = :babyId AND weekNumber = :weekNumber ORDER BY domain ASC")
    fun getMilestonesByWeek(babyId: Long, weekNumber: Int): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE babyId = :babyId ORDER BY weekNumber ASC, domain ASC")
    fun getAllMilestones(babyId: Long): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE babyId = :babyId AND status = 'ACHIEVED' ORDER BY achievedDate DESC")
    fun getAchievedMilestones(babyId: Long): Flow<List<MilestoneEntity>>

    @Query("UPDATE milestones SET status = :status, achievedDate = :achievedDate WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, achievedDate: Long?)

    @Query("UPDATE milestones SET photoUri = :photoUri WHERE id = :id")
    suspend fun updatePhoto(id: Long, photoUri: String)

    /**
     * Gets count of consecutive "NOT_YET" milestones starting from the most recent week.
     * Used to trigger paediatrician advisory (threshold: 3+).
     */
    @Query("""
        SELECT COUNT(*) FROM milestones 
        WHERE babyId = :babyId 
        AND status = 'NOT_YET' 
        AND weekNumber >= :fromWeek
        ORDER BY weekNumber ASC
    """)
    suspend fun getNotYetCountFromWeek(babyId: Long, fromWeek: Int): Int

    @Query("SELECT COUNT(*) FROM milestones WHERE babyId = :babyId AND status = 'ACHIEVED'")
    suspend fun getAchievedCount(babyId: Long): Int

    @Query("SELECT COUNT(*) FROM milestones WHERE babyId = :babyId")
    suspend fun getTotalCount(babyId: Long): Int

    @Query("SELECT DISTINCT weekNumber FROM milestones WHERE babyId = :babyId ORDER BY weekNumber ASC")
    fun getAvailableWeeks(babyId: Long): Flow<List<Int>>

    @Query("DELETE FROM milestones WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: Long)
}
