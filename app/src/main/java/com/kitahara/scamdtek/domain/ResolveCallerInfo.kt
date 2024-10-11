package com.kitahara.scamdtek.domain

import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao

class ResolveCallerInfo(private val dao: RiskWithCommentsDao) {

    operator fun invoke(phoneNumber: String) = dao.getRiskWithComments(phoneNumber)
}