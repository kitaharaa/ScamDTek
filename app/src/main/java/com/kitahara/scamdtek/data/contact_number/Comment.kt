package com.kitahara.scamdtek.data.contact_number

import org.joda.time.DateTime

data class Comment(
    val rank: Rank,
    val text: String,
    val addedAt: DateTime,
)