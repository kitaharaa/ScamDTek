package com.kitahara.scamdtek.presentation.contact_detail

data class PhoneNumberRiskDetails(
    val riskDegree: String?,
    val comments: List<Comment>,
)