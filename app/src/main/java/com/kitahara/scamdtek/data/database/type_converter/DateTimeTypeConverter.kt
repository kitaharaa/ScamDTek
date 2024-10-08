package com.kitahara.scamdtek.data.database.type_converter

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateTimeTypeConverter {

    @TypeConverter
    fun fromDateTime(millis: Long): DateTime {
        return DateTime(millis)
    }

    @TypeConverter
    fun dateTimeToStamp(dateTime: DateTime): Long {
        return dateTime.millis
    }
}