package com.kitahara.scamdtek.presentation.overlay

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import com.kitahara.scamdtek.R
import com.kitahara.scamdtek.common.toast
import com.kitahara.scamdtek.presentation.contact_detail.ContactDetailActivity.Companion.launchContactDetailActivity

class OverlayService : Service() {
    private val windowManager: WindowManager by lazy { getSystemService(WindowManager::class.java) }
    private val vibratorManager: Vibrator by lazy { getSystemService(Vibrator::class.java) as Vibrator }
    private var floatyView: ImageButton? = null
    private var recycleBinView: ImageView? = null
    private var contactNumber: String? = null
    private lateinit var overlayParams: WindowManager.LayoutParams
    private lateinit var recycleBinParams: WindowManager.LayoutParams

    private val screenWidth get() = windowManager.currentWindowMetrics.bounds.width()
    private val screenHeight get() = windowManager.currentWindowMetrics.bounds.height()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        addOverlayView()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        contactNumber = intent?.extras?.getString(EXTRA_PHONE_NUMBER)
        return START_NOT_STICKY
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addOverlayView() {
        val layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        overlayParams = WindowManager.LayoutParams(
            OVERLAY_SIZE,
            OVERLAY_SIZE,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        recycleBinParams = WindowManager.LayoutParams(
            RECYCLE_BIN_SIZE,
            RECYCLE_BIN_SIZE,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        setupView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        val inflater = getSystemService(LayoutInflater::class.java)
        floatyView = inflater.inflate(R.layout.floating_view, null) as ImageButton?
        recycleBinView = inflater.inflate(R.layout.view_recycle_bin, null) as ImageView?
        floatyView?.setOnTouchListener { _, event ->
            when (event.action) {
                // Called when moving action has started
                MotionEvent.ACTION_DOWN -> {
                    recycleBinView?.visibility = View.VISIBLE
                    // Remember initial position and touch point
                    initialX = overlayParams.x
                    initialY = overlayParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    // Calculate new X and Y coordinates of the view
                    overlayParams.x = initialX + (initialTouchX - event.rawX).toInt()
                    overlayParams.y = initialY + (event.rawY - initialTouchY).toInt()
                    // Update the layout with the new X and Y coordinates
                    windowManager.updateViewLayout(floatyView, overlayParams)
                    changeRecycleBinColor(overlayParams.x, overlayParams.y)
                    return@setOnTouchListener true
                }

                // Called when pressed state is ended
                MotionEvent.ACTION_UP -> {
                    recycleBinView?.visibility = View.GONE
                    when {
                        initialX == overlayParams.x && initialY == overlayParams.y -> launchDetailActivity()
                        isOverlayOverlapRecycleBin(overlayParams.x, overlayParams.y) -> {
                            removeViews()
                            onDestroy()
                        }

                        else -> moveOverlayToEdge()
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        windowManager.addView(
            floatyView,
            overlayParams.apply { gravity = Gravity.CENTER or Gravity.END })
        windowManager.addView(
            recycleBinView,
            recycleBinParams.apply { gravity = Gravity.CENTER or Gravity.BOTTOM })
    }

    private fun changeRecycleBinColor(overlayX: Int, overlayY: Int) {
        val colorRes =
            if (isOverlayOverlapRecycleBin(overlayX, overlayY)) {
                vibrate()
                R.color.red
            } else R.color.gray
        val color = getColor(colorRes)
        recycleBinView?.setColorFilter(color)
    }

    private fun isOverlayOverlapRecycleBin(overlayX: Int, overlayY: Int): Boolean {
        val recycleBinStartX = (screenWidth - RECYCLE_BIN_SIZE) / 2
        val recycleBinEndX = recycleBinStartX + RECYCLE_BIN_SIZE
        val recycleBinRangeX = recycleBinStartX..recycleBinEndX

        val recycleBinStartY = (screenHeight - RECYCLE_BIN_SIZE) / 2
        val recycleBinEndY = recycleBinStartY - RECYCLE_BIN_SIZE / 2
        val recycleBinRangeY = recycleBinEndY..recycleBinStartY

        val overlayCenterX = overlayX + OVERLAY_SIZE / 2
        val overlayCenterY = overlayY + OVERLAY_SIZE / 2

        return overlayCenterX in recycleBinRangeX && overlayCenterY in recycleBinRangeY
    }

    private fun moveOverlayToEdge() {
        // Move overlay to start or end by X if it was triggered
        val finalXCoordinate = if (overlayParams.x >= (screenWidth / 2)) screenWidth else 0

        // Animate position by updating the params.x directly
        val startX = overlayParams.x
        ValueAnimator.ofInt(startX, finalXCoordinate).apply {
            duration = OVERLAY_TRANSITION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                overlayParams.x = animation.animatedValue as Int
                windowManager.updateViewLayout(floatyView, overlayParams)
            }
            start()
        }
    }

    private fun launchDetailActivity() {
        if (contactNumber != null) {
            launchContactDetailActivity(contactNumber!!)
        } else toast("Contact number cannot be null")
        removeViews()
        onDestroy() // Finish service
    }

    private fun removeViews() {
        floatyView?.run {
            windowManager.removeView(this)
            null // Clear variable as well
        }
        recycleBinView?.run {
            windowManager.removeView(this)
            null
        }
    }

    private fun vibrate(duration: Long = 50L) {
        try {
            val effect = VibrationEffect.createOneShot(duration, 150)
            vibratorManager.vibrate(effect)
        } catch (_: Exception) {
        }
    }

    companion object {
        private const val EXTRA_PHONE_NUMBER = "PhoneNumber"
        private const val OVERLAY_TRANSITION_DURATION = 300L
        private const val RECYCLE_BIN_SIZE = 175
        private const val OVERLAY_SIZE = 150

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
