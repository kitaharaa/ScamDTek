package com.kitahara.scamdtek.data.caller_info

import com.kitahara.scamdtek.common.logDebug
import com.kitahara.scamdtek.common.logError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CallerInfoApi {

    suspend fun fetchDetails(phoneNumber: String): Document? =
        withContext(Dispatchers.IO) {
            try {
                val url = String.format(GET_PHONE_NUMBER_DETAILS_URL, phoneNumber)
                fetchDocument(url)
            } catch (e: Exception) {
                logError(e.message)
                null
            }
        }

    private fun fetchDocument(url: String): Document? {
        logDebug("Fetching data: $url")
        var attempts = 0

        while (attempts < MAX_RETRIES) {
            try {
                return Jsoup.connect(url).get()
            } catch (e: Exception) {
                attempts++
                println("Attempt $attempts failed: ${e.message}")
            }
        }

        return null
    }

    companion object {
        const val GET_PHONE_NUMBER_DETAILS_URL = "https://www.telefonnyjdovidnyk.com.ua/nomer/%s"
        const val MAX_RETRIES = 3
    }
}