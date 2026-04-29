package com.shishusneh.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.data.local.entity.BabyProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for baby profile operations.
 * Phase 1 supports a single profile; queries return the first/only profile.
 */
@Dao
interface BabyProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BabyProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: BabyProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: BabyProfileEntity)

    @Query("SELECT * FROM baby_profiles ORDER BY createdAt DESC LIMIT 1")
    fun getActiveProfile(): Flow<BabyProfileEntity?>

    @Query("SELECT * FROM baby_profiles ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveProfileOnce(): BabyProfileEntity?

    @Query("SELECT * FROM baby_profiles WHERE id = :id")
    fun getProfileById(id: Long): Flow<BabyProfileEntity?>

    @Query("SELECT COUNT(*) FROM baby_profiles")
    suspend fun getProfileCount(): Int

    @Query("DELETE FROM baby_profiles")
    suspend fun deleteAll()
}
