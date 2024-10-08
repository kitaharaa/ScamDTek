package com.kitahara.scamdtek.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao
import com.kitahara.scamdtek.data.database.entity.CommentEntity
import com.kitahara.scamdtek.data.database.entity.RiskEntity
import com.kitahara.scamdtek.data.database.type_converter.DateTimeTypeConverter
import com.kitahara.scamdtek.data.database.type_converter.RankTypeConverter

@TypeConverters(value = [DateTimeTypeConverter::class, RankTypeConverter::class])
@Database(entities = [CommentEntity::class, RiskEntity::class], version = 1)
abstract class CallerInfoDatabase : RoomDatabase() {

    abstract fun riskWithCommentsDao(): RiskWithCommentsDao

    companion object {
        private const val DATABASE_NAME = "CallerInfoDatabase"

        fun create(context: Context) = Room.databaseBuilder(
            context,
            CallerInfoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}