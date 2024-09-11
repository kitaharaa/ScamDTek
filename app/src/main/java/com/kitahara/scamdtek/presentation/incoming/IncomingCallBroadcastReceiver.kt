package com.kitahara.scamdtek.presentation.incoming

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.telephony.TelephonyManager
import com.kitahara.scamdtek.common.toast
import com.kitahara.scamdtek.presentation.overlay.OverlayService.Companion.launchOverlayService

class IncomingCallBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            context?.toast("Call Incoming")
        }
        val incomingCallerNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        incomingCallerNumber?.let {
            // Code to handle the incoming number
            context?.toast("Phone number: $incomingCallerNumber")
            if (Settings.canDrawOverlays(context)) {
                // Launch service right away - the user has already previously granted permission
                context?.launchOverlayService(phoneNumber = incomingCallerNumber)
            }
        }
    }
}