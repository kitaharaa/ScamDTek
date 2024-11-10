package com.kitahara.scamdtek.data.caller_info

import androidx.compose.ui.graphics.Color

enum class RiskDegree(val text: String, val color: Color) {
    NOT_DEFINED("Хтозна ", Color.Gray),
    USEFUL("Корисний", Color.Green),
    SAFE("Безпечний", Color.Blue),
    NEUTRAL("Нейтральний", Color.LightGray),
    ANNOYING("Надокучливий", Color.Yellow),
    DANGEROUS("Небезпечний", Color.Red);

    companion object {

        fun parse(rawValue: String?): RiskDegree {
            if (rawValue == null) return NOT_DEFINED
            return try {
                RiskDegree.valueOf(rawValue)
            } catch (_: Exception) {
                NOT_DEFINED
            }
        }
    }
}