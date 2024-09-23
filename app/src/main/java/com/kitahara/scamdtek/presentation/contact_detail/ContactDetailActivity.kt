package com.kitahara.scamdtek.presentation.contact_detail

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ContactDetailActivity : ComponentActivity() {

    // TODO use for query
    private val contactNumber by lazy {
        intent?.extras?.getString(EXTRA_CONTACT_NUMBER)
    }

    companion object {
        const val EXTRA_CONTACT_NUMBER = "ContactNumber"

        private const val GET_PHONE_NUMBER_DETAILS_URL = "https://www.telefonnyjdovidnyk.com.ua/nomer/%s"
        private const val MAX_RETRIES = 3

        fun Context.launchContactDetailActivity(contactNumber: String) {
            // TODO kitaharaa: pass actual incoming phone's number
            fetchPhoneNumberDetails("380673120741")

            val intent = Intent(this, ContactDetailActivity::class.java)
            intent.putExtra(EXTRA_CONTACT_NUMBER, contactNumber)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        private fun fetchPhoneNumberDetails(contactNumber: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = String.format(GET_PHONE_NUMBER_DETAILS_URL, contactNumber)
                    val document = fetchDocumentWithRetries(url)

                    if (document == null) {
                        println("Unable to fetch phone number data")
                        return@launch
                    }

                    // TODO kitaharaa: display this data on UI
                    val riskDetails = PhoneNumberRiskDetails (
                        riskDegree = extractRiskDegree(document),
                        comments = extractComments(document)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun extractRiskDegree(document: Document): String? {
            return document.select("#progress-bar-inner-text").first()?.text()
        }

        private fun extractComments(document: Document): List<Comment> {
            val commentElements = document.select(".comments-container .comment-item")

            return commentElements.map { comment ->
                val rankRaw = comment.select("div.td-rank strong").text()
                val text = comment.select("div.comment p.comment-text").text()
                val addedAtRaw = comment.select("div.comment span.date").text()

                val rank = Rank.parse(rankRaw)
                val addedAt = DateTime.parse(addedAtRaw, DateTimeFormat.forPattern("dd.MM.yyyy"))

                Comment(rank = rank, text = text, addedAt = addedAt)
            }
        }

        private fun fetchDocumentWithRetries(url: String): Document? {
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
    }
}

