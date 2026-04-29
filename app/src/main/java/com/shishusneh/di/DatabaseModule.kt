package com.shishusneh.di

import android.content.Context
import androidx.room.Room
import com.shishusneh.data.local.ShishuSnehDatabase
import com.shishusneh.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShishuSnehDatabase {
        // SQLCipher encryption for DPDPA compliance
        // In production, derive passphrase from device-specific key
        System.loadLibrary("sqlcipher")
        val passphrase = "shishu_sneh_secure".toByteArray()
        val factory = SupportOpenHelperFactory(passphrase)

        return Room.databaseBuilder(
            context,
            ShishuSnehDatabase::class.java,
            ShishuSnehDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideBabyProfileDao(db: ShishuSnehDatabase): BabyProfileDao = db.babyProfileDao()
    @Provides fun provideGrowthEntryDao(db: ShishuSnehDatabase): GrowthEntryDao = db.growthEntryDao()
    @Provides fun provideVaccinationDao(db: ShishuSnehDatabase): VaccinationDao = db.vaccinationDao()
    @Provides fun provideMilestoneDao(db: ShishuSnehDatabase): MilestoneDao = db.milestoneDao()
    @Provides fun provideFeedingLogDao(db: ShishuSnehDatabase): FeedingLogDao = db.feedingLogDao()
    @Provides fun provideGenAIConversationDao(db: ShishuSnehDatabase): GenAIConversationDao = db.genAIConversationDao()
}
