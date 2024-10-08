package com.kitahara.scamdtek.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kitahara.scamdtek.data.database.entity.CommentEntity
import com.kitahara.scamdtek.data.database.entity.RiskEntity
import com.kitahara.scamdtek.data.database.entity.RiskWithCommentsEntity

@Dao
interface RiskWithCommentsDao {

    @Upsert
    suspend fun insert(riskEntity: RiskEntity)

    @Upsert
    suspend fun insert(comments: List<CommentEntity>)

    @Transaction
    @Query("SELECT * FROM RiskEntity where phone_number=:phoneNumber")
    suspend fun getUsersWithPlaylists(phoneNumber: String): List<RiskWithCommentsEntity>

    @Transaction
    suspend fun insert(riskEntity: RiskEntity, comments: List<CommentEntity>) {
        insert(riskEntity)
        insert(comments)
    }
}