package com.kitahara.scamdtek.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kitahara.scamdtek.data.contact_number.RiskDegree
import org.joda.time.DateTime

@Entity
data class CommentEntity(
    @PrimaryKey
    @ColumnInfo(PRIMARY_KEY)
    val commentId: Int = 0,
    @ColumnInfo(RiskEntity.PRIMARY_KEY)
    val phoneNumber: String,
    val riskDegree: RiskDegree,
    val dateTime: DateTime,
    val text: String
) {
    companion object {
        const val PRIMARY_KEY = "comment_id"
    }
}