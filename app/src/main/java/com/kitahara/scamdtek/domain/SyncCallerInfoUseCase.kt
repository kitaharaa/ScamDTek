package com.kitahara.scamdtek.domain

import com.kitahara.scamdtek.data.contact_number.CallerInfoRepository
import com.kitahara.scamdtek.data.contact_number.Comment
import com.kitahara.scamdtek.data.contact_number.Comment.Companion.toEntity
import com.kitahara.scamdtek.data.contact_number.RiskDegree
import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao
import com.kitahara.scamdtek.data.database.entity.RiskEntity
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.nodes.Document

class SyncCallerInfoUseCase(
    private val callerInfoRepository: CallerInfoRepository,
    private val riskWithCommentsDao: RiskWithCommentsDao
) {

    suspend operator fun invoke(contactNumber: String) {
        val document = callerInfoRepository.fetchPhoneNumberDetails(contactNumber)
        if (document == null) {
            riskWithCommentsDao.insert(RiskEntity(phoneNumber =  contactNumber, riskDegree = RiskDegree.NOT_DEFINED))
        } else {
            val riskDegreeRaw = extractRiskDegree(document)
            val comments = extractComments(document)
            val riskEntity = RiskEntity(phoneNumber = contactNumber, riskDegree = RiskDegree.parse(riskDegreeRaw))
            riskWithCommentsDao.insert(riskEntity, comments.toEntity(contactNumber))
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

            val riskDegree = RiskDegree.parse(rankRaw)
            val addedAt = DateTime.parse(addedAtRaw, DateTimeFormat.forPattern("dd.MM.yyyy"))

            Comment(riskDegree = riskDegree, text = text, addedAt = addedAt)
        }
    }
}