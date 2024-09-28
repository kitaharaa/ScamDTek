package com.kitahara.scamdtek.domain

import com.kitahara.scamdtek.data.contact_number.CallerInfoRepository
import com.kitahara.scamdtek.data.contact_number.Comment
import com.kitahara.scamdtek.data.contact_number.PhoneNumberRiskDetails
import com.kitahara.scamdtek.data.contact_number.Rank
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.nodes.Document

class GetCallerInfoUseCase(
    private val callerInfoRepository: CallerInfoRepository
) {

    suspend operator fun invoke(contactNumber: String): PhoneNumberRiskDetails? {
        val document = callerInfoRepository.fetchPhoneNumberDetails(contactNumber)
        return document?.let {
            PhoneNumberRiskDetails(
                riskDegree = extractRiskDegree(document),
                comments = extractComments(document)
            )
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
}