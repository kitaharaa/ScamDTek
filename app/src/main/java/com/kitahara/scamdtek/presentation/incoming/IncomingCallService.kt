package com.kitahara.scamdtek.presentation.incoming

import android.telecom.Call
import android.telecom.CallScreeningService
import com.kitahara.scamdtek.common.logDebug
import com.kitahara.scamdtek.domain.SyncCallerInfoUseCase
import com.kitahara.scamdtek.presentation.overlay.OverlayService.Companion.launchOverlayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class IncomingCallService : CallScreeningService() {

    private val syncCallerDetailsUseCase by inject<SyncCallerInfoUseCase>()

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle.schemeSpecificPart
        // Scope used to not be canceled when service is destroyed
        CoroutineScope(Dispatchers.IO).launch {
            syncCallerDetailsUseCase(phoneNumber)
        }
        logDebug("onScreenCall: $phoneNumber")
        launchOverlayService(phoneNumber)
    }
}