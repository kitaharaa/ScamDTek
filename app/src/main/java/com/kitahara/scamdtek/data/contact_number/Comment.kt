package com.kitahara.scamdtek.data.contact_number

import com.kitahara.scamdtek.data.database.entity.CommentEntity
import org.joda.time.DateTime

data class Comment(
    val rank: Rank,
    val text: String,
    val addedAt: DateTime,
) {
    companion object {

        fun List<Comment>.toEntity(phoneNumber: String) =
            map { comment ->
                CommentEntity(
                    phoneNumber = phoneNumber,
                    rank = comment.rank,
                    dateTime = comment.addedAt,
                    text = comment.text
                )
            }

        fun List<CommentEntity>.toWrapper() =
            map { entity ->
                Comment(
                    rank = entity.rank,
                    addedAt = entity.dateTime,
                    text = entity.text,
                )
            }
    }
}