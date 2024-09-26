package com.kitahara.scamdtek.common

import android.util.Log

fun <T: Any> T.logError(message: String) {
    Log.e(this::class.simpleName, message)
}

fun <T: Any> T.logDebug(message: String) {
    Log.d(this::class.simpleName, message)
}