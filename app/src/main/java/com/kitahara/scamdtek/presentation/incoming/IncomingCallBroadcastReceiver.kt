package com.kitahara.scamdtek.presentation.incoming

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class IncomingCallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            Toast.makeText(context, "Call Incoming", Toast.LENGTH_SHORT).show()
        }

        val incomingCallerNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        if (incomingCallerNumber != null) {
            // Code to handle the incoming number
        }
    }
}