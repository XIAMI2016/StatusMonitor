package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.view.KeyEvent
import androidx.annotation.RequiresApi


/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
internal class VolumeListener(
        private val context: Context,
        private val callback : OnVolumeCallback
) : Listener {

    private var receiver : VolumeBroadcastReceiver? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun registerListener() {
        receiver = VolumeBroadcastReceiver.buildAndRegister(context, callback)
    }

    override fun unregisterListener() {
        if(receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    fun getCurrentVolume(streamType: Int) : Int {
        return audioManager.getStreamVolume(streamType)
    }

    fun getMaxVolume(streamType: Int) : Int {
        return audioManager.getStreamMaxVolume(streamType)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getMinVolume(streamType: Int) : Int {
        return audioManager.getStreamMinVolume(streamType)
    }

    class VolumeBroadcastReceiver(private val callback: OnVolumeCallback? = null) : BroadcastReceiver() {

        companion object {

            private const val ACTION_VOLUME = "android.media.VOLUME_CHANGED_ACTION"
            private const val KEY_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE"
            private const val KEY_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE"

            fun buildAndRegister(context: Context,callback: OnVolumeCallback? = null) : VolumeBroadcastReceiver {
                val receiver = VolumeBroadcastReceiver(callback)
                val filter = IntentFilter().apply {
                    addAction(ACTION_VOLUME)
                }
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_VOLUME -> {
                    val streamType = intent.getIntExtra(KEY_STREAM_TYPE, -1)
                    val volume = intent.getIntExtra(KEY_STREAM_VALUE, -1)
                    callback?.onVolumeChanged(streamType, volume)
                }
            }
        }

    }
}