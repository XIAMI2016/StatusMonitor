package com.terence.monitor.status

import android.content.Context
import android.os.Build
import android.view.KeyEvent
import androidx.annotation.RequiresApi

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
internal class VolumeMonitor(context: Context) : OnVolumeCallback  {

    private val volumeListener = VolumeListener(context, this)
    private val onCallbackList = mutableListOf<OnVolumeCallback>()

    fun addCallback(onVolumeCallback: OnVolumeCallback){
        if(onCallbackList.contains(onVolumeCallback)) return
        onCallbackList.ifEmpty { volumeListener.registerListener() }
        onCallbackList.add(onVolumeCallback)
    }

    fun removeCallback(onVolumeCallback: OnVolumeCallback){
        if(!onCallbackList.contains(onVolumeCallback)) return
        onCallbackList.remove(onVolumeCallback)
        onCallbackList.ifEmpty { volumeListener.unregisterListener() }
    }

    fun getCurrentVolume(streamType: Int) : Int {
        return volumeListener.getCurrentVolume(streamType)
    }

    fun getMaxVolume(streamType: Int) : Int {
        return volumeListener.getMaxVolume(streamType)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getMinVolume(streamType: Int) : Int {
        return volumeListener.getMinVolume(streamType)
    }

    override fun onVolumeChanged(streamType: Int, volume: Int) {
        onCallbackList.forEach { it.onVolumeChanged(streamType, volume) }
    }
}