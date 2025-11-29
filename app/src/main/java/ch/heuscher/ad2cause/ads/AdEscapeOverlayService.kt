package ch.heuscher.ad2cause.ads

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import ch.heuscher.ad2cause.R

/**
 * Overlay service that displays an escape button during ad playback.
 * Allows users to always have a way to close ads.
 */
class AdEscapeOverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    companion object {
        private const val TAG = "AdEscapeOverlayService"
        const val ACTION_SHOW = "ch.heuscher.ad2cause.ACTION_SHOW_ESCAPE"
        const val ACTION_HIDE = "ch.heuscher.ad2cause.ACTION_HIDE_ESCAPE"
        
        // Callback for when escape button is pressed
        var onEscapePressed: (() -> Unit)? = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Service created")
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: action=${intent?.action}")
        when (intent?.action) {
            ACTION_SHOW -> showOverlay()
            ACTION_HIDE -> hideOverlay()
        }
        return START_NOT_STICKY
    }

    private fun showOverlay() {
        Log.d(TAG, "showOverlay: Attempting to show overlay, current overlayView=$overlayView")
        if (overlayView != null) {
            Log.d(TAG, "showOverlay: Overlay already visible, skipping")
            return
        }

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_ad_escape, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 16
            y = 100
        }

        overlayView?.findViewById<ImageButton>(R.id.escapeButton)?.setOnClickListener {
            Log.d(TAG, "showOverlay: Escape button clicked!")
            onEscapePressed?.invoke()
            hideOverlay()
        }

        try {
            windowManager?.addView(overlayView, params)
            Log.d(TAG, "showOverlay: Overlay added successfully")
        } catch (e: Exception) {
            Log.e(TAG, "showOverlay: Failed to add overlay", e)
            overlayView = null
        }
    }

    private fun hideOverlay() {
        Log.d(TAG, "hideOverlay: Attempting to hide overlay")
        overlayView?.let {
            try {
                windowManager?.removeView(it)
                Log.d(TAG, "hideOverlay: Overlay removed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "hideOverlay: Failed to remove overlay", e)
            }
            overlayView = null
        }
        stopSelf()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Service destroyed")
        hideOverlay()
        super.onDestroy()
    }
}
