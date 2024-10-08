package com.kitahara.scamdtek.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RiskWithCommentsEntity(
    @Embedded
    val risk: RiskEntity,
    @Relation(
        parentColumn = RiskEntity.PRIMARY_KEY,
        entityColumn = CommentEntity.PRIMARY_KEY
    )
    val comments: List<CommentEntity>
)