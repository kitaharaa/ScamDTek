package com.kitahara.scamdtek.presentation.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.kitahara.scamdtek.R
import com.kitahara.scamdtek.common.logDebug
import com.kitahara.scamdtek.common.toast
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailActivity.Companion.launchContactDetailActivity

/**
 * Copied from [repository](https://github.com/noln/system-alert-window-example/blob/master/app/src/main/java/com/mattfenlon/ghost/MainService.java) with some adjustments
 */
class OverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var floatyView: View? = null
    private var contactNumber: String? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WindowManager::class.java)
        addOverlayView()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        contactNumber = intent?.extras?.getString(EXTRA_PHONE_NUMBER)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addOverlayView() {
        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER or Gravity.END
        }
        val inflater = getSystemService(LayoutInflater::class.java)
        floatyView = inflater.inflate(R.layout.floating_view, null)

        floatyView?.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                logDebug("Action triggered: ${event.action}")
                when (event.action) {
                    // Called when moving action has started
                    MotionEvent.ACTION_DOWN -> {
                        // Remember initial position and touch point
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // Calculate new X and Y coordinates of the view
                        params.x = initialX + (initialTouchX - event.rawX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()

                        // Update the layout with the new X and Y coordinates
                        windowManager?.updateViewLayout(floatyView, params)
                        return true
                    }

                    // Called when pressed state is ended
                    MotionEvent.ACTION_UP -> {
                        if (initialX == params.x && initialY == params.y)
                            launchDetailActivity()
                        return true
                    }
                }
                return false
            }
        })
        windowManager?.addView(floatyView, params)
    }

    private fun launchDetailActivity() {
        if (contactNumber != null) {
            launchContactDetailActivity(contactNumber!!)
        } else {
            toast("Contact number cannot be null")
            onDestroy() // Finish service
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        floatyView?.run {
            windowManager?.removeView(floatyView)
            null // Clear variable as well
        }
    }

    companion object {
        private const val EXTRA_PHONE_NUMBER = "PhoneNumber"

        fun Context.launchOverlayService(phoneNumber: String) {
            val overlayServiceIntent = Intent(this, OverlayService::class.java)
            overlayServiceIntent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
            stopService(overlayServiceIntent)
            startService(overlayServiceIntent)
        }

        fun Context.stopOverlayService() {
            val overlayServiceIntent = Intent(this, OverlayService::class.java)
            stopService(overlayServiceIntent)
        }
    }
}
