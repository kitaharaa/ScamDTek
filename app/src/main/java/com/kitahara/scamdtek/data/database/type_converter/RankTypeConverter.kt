package com.kitahara.scamdtek.data.database.type_converter

import androidx.room.TypeConverter
import com.kitahara.scamdtek.data.contact_number.Rank

class RankTypeConverter {

    @TypeConverter
    fun fromRankToString(rank: Rank): String {
        return rank.text
    }

    @TypeConverter
    fun fromStringToRank(string: String): Rank {
        return Rank.parse(string)
    }
}