package com.kitahara.scamdtek.domain.model

import com.kitahara.scamdtek.data.caller_info.Comment
import com.kitahara.scamdtek.data.caller_info.Comment.Companion.toWrapper
import com.kitahara.scamdtek.data.caller_info.RiskDegree
import com.kitahara.scamdtek.data.database.entity.RiskWithCommentsEntity

data class CallerInfoItem(
    val number: String,
    val riskDegree: RiskDegree,
    val comments: List<Comment>,
    val commentCount: Int
) {
    companion object {
        fun RiskWithCommentsEntity.toCallerInfoItem() =
            CallerInfoItem(
                number = risk.phoneNumber,
                riskDegree = risk.riskDegree,
                comments = comments?.toWrapper() ?: emptyList(),
                commentCount = comments?.size ?: 0
            )

        fun List<RiskWithCommentsEntity>.toCallerInfoItems() = map { entity ->
            CallerInfoItem(
                number = entity.risk.phoneNumber,
                riskDegree = entity.risk.riskDegree,
                comments = entity.comments?.toWrapper() ?: emptyList(),
                commentCount = entity.comments?.size ?: 0
            )
        }
    }
}
