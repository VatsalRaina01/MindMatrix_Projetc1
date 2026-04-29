package com.shishusneh.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.shishusneh.data.repository.BabyProfileRepository
import com.shishusneh.data.repository.VaccinationRepository
import com.shishusneh.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * WorkManager worker for vaccination reminders.
 * Fires at 7 days, 3 days, and day-of for each upcoming vaccine.
 * Survives device reboot and battery optimization.
 */
@HiltWorker
class VaccinationReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val profileRepo: BabyProfileRepository,
    private val vaccinationRepo: VaccinationRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val profile = profileRepo.getActiveProfileOnce() ?: return Result.success()

        // Update overdue status
        val now = System.currentTimeMillis()
        vaccinationRepo.updateOverdueStatus(profile.id, now)

        // Check for vaccines due in 7 days, 3 days, and today
        val intervals = listOf(7, 3, 0)
        for (daysAhead in intervals) {
            val startOfDay = now + (daysAhead * 24 * 60 * 60 * 1000L)
            val endOfDay = startOfDay + (24 * 60 * 60 * 1000L)
            val upcoming = vaccinationRepo.getUpcomingInRange(profile.id, startOfDay, endOfDay)

            for (vax in upcoming) {
                val notification = NotificationHelper.buildVaccinationNotification(
                    applicationContext, vax.vaccineName, vax.disease, daysAhead
                ).build()

                val notifManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager
                notifManager.notify(vax.id.toInt(), notification)
            }
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME = "vaccination_reminder_daily"

        /**
         * Schedule daily check for upcoming vaccinations.
         * Runs once per day, checks for vaccines due in 7/3/0 days.
         */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)  // Critical health reminders should fire even on low battery
                .build()

            val request = PeriodicWorkRequestBuilder<VaccinationReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setInitialDelay(1, TimeUnit.HOURS) // First check 1 hour after scheduling
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }
}
