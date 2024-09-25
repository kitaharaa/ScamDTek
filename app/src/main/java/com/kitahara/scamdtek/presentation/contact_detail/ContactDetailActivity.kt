package com.kitahara.scamdtek.presentation.contact_detail

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.kitahara.scamdtek.common.theme.ScamDTekTheme

class ContactDetailActivity : ComponentActivity() {

    private val contactNumber by lazy {
        intent?.extras?.getString(EXTRA_CONTACT_NUMBER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamDTekTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = "And welcome to \nLos Pollos Hermanosn\n$contactNumber"
                    )
                }
            }
        }
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