package com.terence.monitor.status

import android.content.Context

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
internal class TimeChangeMonitor(context: Context) : OnTimeChangeCallback {

    private val timeChangeListener = TimeChangeListener(context, this)
    private val onCallbackList = mutableListOf<OnTimeChangeCallback>()


    fun addCallback(onTimeChangeCallback: OnTimeChangeCallback){
        if(onCallbackList.contains(onTimeChangeCallback)) return
        onCallbackList.ifEmpty { timeChangeListener.registerListener() }
        onCallbackList.add(onTimeChangeCallback)
    }

    fun removeCallback(onTimeChangeCallback: OnTimeChangeCallback){
        if(!onCallbackList.contains(onTimeChangeCallback)) return
        onCallbackList.remove(onTimeChangeCallback)
        onCallbackList.ifEmpty { timeChangeListener.unregisterListener() }
    }

    override fun onTickMinute() {
        onCallbackList.forEach { it.onTickMinute() }
    }

    override fun onTimeChanged() {
        onCallbackList.forEach { it.onTimeChanged() }
    }

    override fun onTimeZoneChanged() {
        onCallbackList.forEach { it.onTimeZoneChanged() }
    }
}