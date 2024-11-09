package com.kitahara.scamdtek.data.contact_number

import com.kitahara.scamdtek.data.database.entity.CommentEntity
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

data class Comment(
    val riskDegree: RiskDegree,
    val text: String,
    val addedAt: DateTime,
) {
    val formatDate: String get() = DateTimeFormat.forPattern("dd.MM.yy").print(addedAt)

    companion object {

        fun List<Comment>.toEntity(phoneNumber: String) =
            map { comment ->
                CommentEntity(
                    phoneNumber = phoneNumber,
                    riskDegree = comment.riskDegree,
                    dateTime = comment.addedAt,
                    text = comment.text
                )
            }

        fun List<CommentEntity>.toWrapper() =
            map { entity ->
                Comment(
                    riskDegree = entity.riskDegree,
                    addedAt = entity.dateTime,
                    text = entity.text,
                )
            }
    }
}