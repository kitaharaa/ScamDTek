package com.kitahara.scamdtek.domain

import com.kitahara.scamdtek.data.database.dao.RiskWithCommentsDao
import com.kitahara.scamdtek.domain.model.CallerInfoItem
import com.kitahara.scamdtek.domain.model.CallerInfoItem.Companion.toCallerInfoItem
import com.kitahara.scamdtek.domain.model.CallerInfoItem.Companion.toCallerInfoItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ResolveCallerInfoItemUseCase(private val dao: RiskWithCommentsDao) {
    fun getCallerInfoWithComments(phoneNumber: String): Flow<CallerInfoItem?> =
        dao.getCallerInfoWithComments(phoneNumber).map { it?.toCallerInfoItem() }

    fun getAllCallersInfoWithComments(): Flow<List<CallerInfoItem>> =
        dao.getAllCallersInfoWithComments().map { it.toCallerInfoItems() }
}