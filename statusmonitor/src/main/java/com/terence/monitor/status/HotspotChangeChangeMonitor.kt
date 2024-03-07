package com.terence.monitor.status

import android.content.Context
import androidx.annotation.RequiresPermission

/**
 *   Created by Terence.J.Tang on 2024/3/5
 */
internal class HotspotChangeChangeMonitor(private val context: Context) : OnHotspotChangeCallback {

    private val hotspotChangeListener = HotspotChangeListener(context, this)
    private val onHotspotCallbackList = mutableListOf<OnHotspotChangeCallback>()

    @RequiresPermission(value = "android.permission.ACCESS_WIFI_STATE")
    fun addCallbackList(onHotspotChangeCallback: OnHotspotChangeCallback) {
        onHotspotCallbackList.ifEmpty { hotspotChangeListener.registerListener() }

        if(hotspotChangeListener.isApEnable(context)){
            onHotspotChangeCallback.onHotspotEnabled()
        }else {
            onHotspotChangeCallback.onHotspotDisabled()
        }

        onHotspotCallbackList.add(onHotspotChangeCallback)
    }

    fun removeCallbackList(onHotspotChangeCallback: OnHotspotChangeCallback) {
        onHotspotCallbackList.remove(onHotspotChangeCallback)
        onHotspotCallbackList.ifEmpty { hotspotChangeListener.unregisterListener() }
    }

    @RequiresPermission(value = "android.permission.ACCESS_WIFI_STATE")
    fun isApEnable() = hotspotChangeListener.isApEnable(context)

    override fun onHotspotDisabled() {
        onHotspotCallbackList.forEach { it.onHotspotDisabled() }
    }

    override fun onHotspotEnabled() {
        onHotspotCallbackList.forEach { it.onHotspotEnabled() }
    }

    override fun onHotspotDisabling() {
        onHotspotCallbackList.forEach { it.onHotspotDisabling() }
    }

    override fun onHotspotEnabling() {
        onHotspotCallbackList.forEach { it.onHotspotEnabling() }
    }
}