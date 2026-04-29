package com.shishusneh.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shishusneh.data.local.converter.Converters
import com.shishusneh.data.local.dao.BabyProfileDao
import com.shishusneh.data.local.dao.FeedingLogDao
import com.shishusneh.data.local.dao.GenAIConversationDao
import com.shishusneh.data.local.dao.GrowthEntryDao
import com.shishusneh.data.local.dao.MilestoneDao
import com.shishusneh.data.local.dao.VaccinationDao
import com.shishusneh.data.local.entity.BabyProfileEntity
import com.shishusneh.data.local.entity.FeedingLogEntity
import com.shishusneh.data.local.entity.GenAIConversationEntity
import com.shishusneh.data.local.entity.GrowthEntryEntity
import com.shishusneh.data.local.entity.MilestoneEntity
import com.shishusneh.data.local.entity.VaccinationEntity

/**
 * Room database for Shishu-Sneh.
 * Encrypted with SQLCipher for DPDPA compliance.
 * Version 1 — initial schema with all 6 entities.
 */
@Database(
    entities = [
        BabyProfileEntity::class,
        GrowthEntryEntity::class,
        VaccinationEntity::class,
        MilestoneEntity::class,
        FeedingLogEntity::class,
        GenAIConversationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ShishuSnehDatabase : RoomDatabase() {
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun growthEntryDao(): GrowthEntryDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun feedingLogDao(): FeedingLogDao
    abstract fun genAIConversationDao(): GenAIConversationDao

    companion object {
        const val DATABASE_NAME = "shishu_sneh_db"
    }
}
