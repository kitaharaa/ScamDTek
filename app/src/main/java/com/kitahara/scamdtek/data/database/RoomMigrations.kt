package com.kitahara.scamdtek.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object RoomMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE CommentEntity RENAME COLUMN rank TO riskDegree")
            db.execSQL("UPDATE CommentEntity SET riskDegree = 'хтозна' WHERE riskDegree IS NULL;")
            db.execSQL(
                """
                CREATE TABLE temp_table (
                    id INTEGER PRIMARY KEY,
                    dateTime INTEGER,
                    phone_number INTEGER,
                    riskDegree TEXT,
                    text TEXT,
                    comment_id INTEGER
                );
                
                INSERT INTO temp_table (
                                       dateTime,
                                       phone_number,
                                       riskDegree,
                                       text,
                                       comment_id
                                   )
                                   SELECT dateTime,
                                       phone_number,
                                       riskDegree,
                                       text,
                                       comment_id
                                     FROM temp_table;
                                     
                DROP TABLE CommentEntity;
                
                temp_table RENAME TO CommentEntity;
                           """.trimIndent()
            )

            // Migration for RiskEntity
            db.execSQL("UPDATE RiskEntity SET riskDegree = 'хтозна' WHERE riskDegree IS NULL;")

            // Create a temporary table for RiskEntity
            db.execSQL(
                """
                        CREATE TABLE temp_risktable (
                            phone_number TEXT PRIMARY KEY NOT NULL,
                            riskDegree TEXT NOT NULL
                        );
            """
            )
            db.execSQL(
                """
                        INSERT INTO temp_risktable (
                            phone_number, riskDegree
                        )
                        SELECT phone_number, riskDegree
                        FROM RiskEntity;
            """
            )
            db.execSQL("DROP TABLE RiskEntity")
            db.execSQL("ALTER TABLE temp_risktable RENAME TO RiskEntity")
        }
    }
}