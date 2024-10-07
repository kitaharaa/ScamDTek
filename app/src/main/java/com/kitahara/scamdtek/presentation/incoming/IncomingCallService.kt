package com.kitahara.scamdtek.presentation.incoming

import android.telecom.Call
import android.telecom.CallScreeningService
import com.kitahara.scamdtek.common.logDebug
import com.kitahara.scamdtek.presentation.overlay.OverlayService.Companion.launchOverlayService

class IncomingCallService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle.schemeSpecificPart
        logDebug("onScreenCall: $phoneNumber")
        launchOverlayService(phoneNumber)
    }
}