package com.shishusneh

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.shishusneh.util.NotificationHelper
import com.shishusneh.worker.VaccinationReminderWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for Shishu-Sneh.
 * Initializes Hilt DI, WorkManager, and notification channels.
 */
@HiltAndroidApp
class ShishuSnehApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        // Create notification channels on app startup
        NotificationHelper.createChannels(this)

        // Schedule daily vaccination reminder worker
        VaccinationReminderWorker.schedule(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
