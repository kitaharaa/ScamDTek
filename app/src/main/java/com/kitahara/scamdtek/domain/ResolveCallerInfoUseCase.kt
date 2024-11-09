package com.kitahara.scamdtek.domain

import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao

class ResolveCallerInfoUseCase(private val dao: RiskWithCommentsDao) {
    fun getCallerInfoWithComments(phoneNumber: String) = dao.getCallerInfoWithComments(phoneNumber)
    fun getAllCallersInfoWithComments() = dao.getAllCallersInfoWithComments()
}