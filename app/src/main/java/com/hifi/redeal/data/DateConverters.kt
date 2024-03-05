package com.hifi.redeal.data

import androidx.room.TypeConverter
import java.util.Date


class DateConverters {
    @TypeConverter
    fun fromDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time?.toLong()
    }

}

