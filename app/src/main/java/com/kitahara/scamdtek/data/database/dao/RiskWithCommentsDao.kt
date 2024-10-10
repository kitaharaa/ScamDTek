package com.kitahara.scamdtek.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kitahara.scamdtek.data.database.entity.CommentEntity
import com.kitahara.scamdtek.data.database.entity.RiskEntity
import com.kitahara.scamdtek.data.database.entity.RiskWithCommentsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiskWithCommentsDao {

    @Upsert
    suspend fun insert(riskEntity: RiskEntity)

    @Upsert
    suspend fun insert(comments: List<CommentEntity>)

    @Transaction
    @Query("SELECT * FROM RiskEntity where phone_number=:phoneNumber")
    fun getRiskWithComments(phoneNumber: String): Flow<RiskWithCommentsEntity?>

    @Query("SELECT riskDegree FROM RiskEntity where phone_number=:phoneNumber")
    fun getRisk(phoneNumber: String): Flow<String?>

    @Transaction
    suspend fun insert(riskEntity: RiskEntity, comments: List<CommentEntity>?) {
        insert(riskEntity)
        if (comments != null) insert(comments)
    }
}