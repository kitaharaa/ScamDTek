package com.kitahara.scamdtek.data.contact_number

import androidx.compose.ui.graphics.Color

enum class Rank(val text: String, val color: Color) {
    NOT_DEFINED("Хтозна ", Color.Gray),
    USEFUL("Корисний", Color.Green),
    SAFE("Безпечний", Color.Blue),
    NEUTRAL("Нейтральний", Color.LightGray),
    ANNOYING("Надокучливий", Color.Yellow),
    DANGEROUS("Небезпечний", Color.Red);

    companion object {

        fun parse(rawValue: String): Rank =
            try {
                Rank.valueOf(rawValue)
            } catch (_: Exception) {
                NOT_DEFINED
            }
    }
}