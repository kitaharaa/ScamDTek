package com.kitahara.scamdtek.presentation.contact_detail

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.activity.ComponentActivity

class ContactDetailActivity : ComponentActivity() {

    // TODO use for query
    private val contactNumber by lazy {
        intent?.extras?.getString(EXTRA_CONTACT_NUMBER)
    }

    companion object {
        const val EXTRA_CONTACT_NUMBER = "ContactNumber"

        fun Context.launchContactDetailActivity(contactNumber: String) {
            val intent = Intent(this, ContactDetailActivity::class.java)
            intent.putExtra(EXTRA_CONTACT_NUMBER, contactNumber)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}