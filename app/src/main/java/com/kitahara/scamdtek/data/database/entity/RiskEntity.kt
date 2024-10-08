package com.kitahara.scamdtek.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RiskEntity(
    @PrimaryKey
    @ColumnInfo(PRIMARY_KEY)
    val phoneNumber: String,
    val riskDegree: String?,
) {
    companion object {
        const val PRIMARY_KEY = "phone_number"
    }
}