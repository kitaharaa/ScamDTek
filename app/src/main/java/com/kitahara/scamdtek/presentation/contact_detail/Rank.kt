package com.kitahara.scamdtek.presentation.contact_detail

enum class Rank(val value: String) {
    NOT_DEFINED(""),
    USEFUL("Корисний"),
    SAFE("Безпечний"),
    NEUTRAL("Нейтральний"),
    ANNOYING("Надокучливий"),
    DANGEROUS("Небезпечний");

    companion object {
        fun parse(rawValue: String): Rank {
            return Rank.entries.find { it.value == rawValue } ?: NOT_DEFINED
        }
    }
}