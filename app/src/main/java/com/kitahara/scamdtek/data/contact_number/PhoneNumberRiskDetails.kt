package com.kitahara.scamdtek.data.contact_number

data class PhoneNumberRiskDetails(
    val riskDegree: String?,
    val comments: List<Comment>,
)