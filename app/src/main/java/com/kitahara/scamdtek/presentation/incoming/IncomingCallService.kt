package com.kitahara.scamdtek.presentation.incoming

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.kitahara.scamdtek.presentation.overlay.OverlayService.Companion.launchOverlayService

class IncomingCallService : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle.schemeSpecificPart
        Log.e(this::class.simpleName, "onScreenCall: triggered $phoneNumber", )
        baseContext?.launchOverlayService(phoneNumber)
    }
}