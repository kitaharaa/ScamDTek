package com.kitahara.scamdtek

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kitahara.scamdtek.common.theme.ScamDTekTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamDTekTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        if (!Settings.canDrawOverlays(this)) checkDrawOverlayPermission()
        requestScreeningRole()
    }

    private fun checkDrawOverlayPermission() {
        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this)) {
            // If not, form up an Intent to launch the permission request
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            // Launch Intent, with the supplied request code
            startActivityForResult(intent, OVERLAY_REQUEST_CODE)
        }
    }

    private fun requestScreeningRole() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        val isHeld = roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        if (!isHeld) {
            //ask the user to set your app as the default screening app
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            startActivityForResult(intent, ROLE_MANAGER_REQUEST_CODE)
        }
    }

    companion object {
        private const val OVERLAY_REQUEST_CODE: Int = 10101
        private const val ROLE_MANAGER_REQUEST_CODE: Int = 123
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScamDTekTheme {
        Greeting("Android")
    }
}