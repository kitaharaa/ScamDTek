package com.kitahara.scamdtek.data.database.type_converter

import androidx.room.TypeConverter
import com.kitahara.scamdtek.data.contact_number.RiskDegree

class RiskDegreeTypeConverter {

    @TypeConverter
    fun fromRankToString(riskDegree: RiskDegree): String {
        return riskDegree.text
    }

    @TypeConverter
    fun fromStringToRank(string: String): RiskDegree {
        return RiskDegree.parse(string)
    }
}