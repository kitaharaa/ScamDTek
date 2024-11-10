package com.kitahara.scamdtek.data.contact_number

import com.kitahara.scamdtek.common.logDebug
import com.kitahara.scamdtek.common.logError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

// TODO make from this API abstraction
class CallerInfoRepository {

    suspend fun fetchPhoneNumberDetails(contactNumber: String): Document? =
        withContext(Dispatchers.IO) {
            try {
                val url = String.format(GET_PHONE_NUMBER_DETAILS_URL, contactNumber)
                fetchDocumentWithRetries(url)
            } catch (e: Exception) {
                logError(e.message)
                null
            }
        }

    private fun fetchDocumentWithRetries(url: String): Document? {
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