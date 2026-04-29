package com.shishusneh.data.local.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Room type converters for date/time types.
 * Stores all dates as epoch milliseconds (Long) in UTC.
 */
class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDate(millis: Long?): LocalDate? {
        return millis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
        }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.atZone(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(millis: Long?): LocalDateTime? {
        return millis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDateTime()
        }
    }
}
