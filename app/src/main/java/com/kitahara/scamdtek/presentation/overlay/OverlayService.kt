package com.kitahara.scamdtek.presentation.overlay

import android.animation.ValueAnimator
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
import android.view.animation.AccelerateDecelerateInterpolator
import com.kitahara.scamdtek.R
import com.kitahara.scamdtek.common.toast
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailActivity.Companion.launchContactDetailActivity

class OverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var floatyView: View? = null
    private var contactNumber: String? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WindowManager::class.java)
        addOverlayView()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        contactNumber = intent?.extras?.getString(EXTRA_PHONE_NUMBER)
        return START_NOT_STICKY
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addOverlayView() {
        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER or Gravity.END
        }
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        floatyView?.run {
            windowManager?.removeView(floatyView)
            null // Clear variable as well
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        val inflater = getSystemService(LayoutInflater::class.java)
        floatyView = inflater.inflate(R.layout.floating_view, null)
        floatyView?.setOnTouchListener { _, event ->
            when (event.action) {
                // Called when moving action has started
                MotionEvent.ACTION_DOWN -> {
                    // Remember initial position and touch point
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    // Calculate new X and Y coordinates of the view
                    params.x = initialX + (initialTouchX - event.rawX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    // Update the layout with the new X and Y coordinates
                    windowManager?.updateViewLayout(floatyView, params)
                    return@setOnTouchListener true
                }

                // Called when pressed state is ended
                MotionEvent.ACTION_UP -> {
                    if (initialX == params.x && initialY == params.y) {
                        launchDetailActivity()
                    } else moveOverlayToEdge()
                    return@setOnTouchListener true
                }
            }
            false
        }
        windowManager?.addView(floatyView, params)
    }

    private fun moveOverlayToEdge() {
        // Move overlay to start or end by X if it was triggered
        val metrics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager?.currentWindowMetrics
        } else {
            TODO("VERSION.SDK_INT < R")
        }

        val screenWidth = metrics?.bounds?.width()
        val finalXCoordinate = if (screenWidth == null || params.x >= (screenWidth / 2)) {
            screenWidth ?: 0
        } else {
            0
        }
        // Animate position by updating the params.x directly
        val startX = params.x
        ValueAnimator.ofInt(startX, finalXCoordinate).apply {
            duration = OVERLAY_TRANSITION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                params.x = animation.animatedValue as Int
                windowManager?.updateViewLayout(floatyView, params)
            }
            start()
        }
    }

    private fun launchDetailActivity() {
        if (contactNumber != null) {
            launchContactDetailActivity(contactNumber!!)
        } else {
            toast("Contact number cannot be null")
        }
        onDestroy() // Finish service
    }

    companion object {
        private const val EXTRA_PHONE_NUMBER = "PhoneNumber"
        private const val OVERLAY_TRANSITION_DURATION = 300L

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
