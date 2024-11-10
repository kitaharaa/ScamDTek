package com.kitahara.scamdtek.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kitahara.scamdtek.data.database.RoomMigrations.MIGRATION_1_2
import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao
import com.kitahara.scamdtek.data.database.entity.CommentEntity
import com.kitahara.scamdtek.data.database.entity.RiskEntity
import com.kitahara.scamdtek.data.database.type_converter.DateTimeTypeConverter
import com.kitahara.scamdtek.data.database.type_converter.RiskDegreeTypeConverter

@TypeConverters(value = [DateTimeTypeConverter::class, RiskDegreeTypeConverter::class])
@Database(
    version = 2,
    entities = [CommentEntity::class, RiskEntity::class]
)
abstract class CallerInfoDatabase : RoomDatabase() {

    abstract fun riskWithCommentsDao(): RiskWithCommentsDao

    companion object {
        private const val DATABASE_NAME = "CallerInfoDatabase"

        fun create(context: Context) = Room.databaseBuilder(
            context,
            CallerInfoDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}