package com.kitahara.scamdtek.presentation.contact_detail

import org.joda.time.DateTime

data class Comment(
    val rank: Rank,
    val text: String,
    val addedAt: DateTime,
)