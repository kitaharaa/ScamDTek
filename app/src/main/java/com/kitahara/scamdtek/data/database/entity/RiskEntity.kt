package com.kitahara.scamdtek.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kitahara.scamdtek.data.caller_info.RiskDegree

@Entity
data class RiskEntity(
    @PrimaryKey
    @ColumnInfo(PRIMARY_KEY)
    val phoneNumber: String,
    val riskDegree: RiskDegree,
) {
    companion object {
        const val PRIMARY_KEY = "phone_number"
    }
}