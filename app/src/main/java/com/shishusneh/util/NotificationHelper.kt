package com.shishusneh.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shishusneh.MainActivity
import com.shishusneh.R

/**
 * Handles notification channel creation and notification building.
 * Channels: Vaccination reminders, Feeding reminders, General updates.
 */
object NotificationHelper {

    const val CHANNEL_VACCINATION = "vaccination_reminders"
    const val CHANNEL_FEEDING = "feeding_reminders"
    const val CHANNEL_GENERAL = "general_updates"

    /**
     * Creates all notification channels. Should be called on app startup.
     */
    fun createChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val vaccinationChannel = NotificationChannel(
            CHANNEL_VACCINATION,
            context.getString(R.string.channel_vaccination),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.channel_vaccination_desc)
            enableVibration(true)
            setShowBadge(true)
        }

        val feedingChannel = NotificationChannel(
            CHANNEL_FEEDING,
            context.getString(R.string.channel_feeding),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.channel_feeding_desc)
        }

        val generalChannel = NotificationChannel(
            CHANNEL_GENERAL,
            context.getString(R.string.channel_general),
            NotificationManager.IMPORTANCE_LOW
        )

        manager.createNotificationChannels(listOf(vaccinationChannel, feedingChannel, generalChannel))
    }

    /**
     * Build a vaccination reminder notification.
     */
    fun buildVaccinationNotification(
        context: Context,
        vaccineName: String,
        disease: String,
        daysUntil: Int
    ): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "vaccination")
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = when (daysUntil) {
            0 -> context.getString(R.string.vaccine_due_today, vaccineName)
            1 -> context.getString(R.string.vaccine_due_tomorrow, vaccineName)
            else -> context.getString(R.string.vaccine_due_days, vaccineName, daysUntil)
        }

        return NotificationCompat.Builder(context, CHANNEL_VACCINATION)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.vaccine_prevents, disease))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
    }

    /**
     * Build a feeding reminder notification.
     */
    fun buildFeedingNotification(context: Context): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_FEEDING)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.feeding_reminder_title))
            .setContentText(context.getString(R.string.feeding_reminder_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}
