package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
internal class TimeChangeListener(
        private val context: Context,
        private val callback: OnTimeChangeCallback
) : Listener{

    private var receiver : TimeBroadcastReceiver? = null

    override fun registerListener() {
        receiver = TimeBroadcastReceiver.buildAndRegister(context, callback)
    }

    override fun unregisterListener() {
        if(receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    class TimeBroadcastReceiver(private val callback: OnTimeChangeCallback? = null) : BroadcastReceiver() {

        companion object {
            fun buildAndRegister(context: Context,callback: OnTimeChangeCallback? = null) : TimeBroadcastReceiver {
                val receiver = TimeBroadcastReceiver(callback)
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_TIME_TICK)
                    addAction(Intent.ACTION_TIME_CHANGED)
                    addAction(Intent.ACTION_TIMEZONE_CHANGED)
                }
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }


        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                Intent.ACTION_TIME_TICK -> callback?.onTickMinute()
                Intent.ACTION_TIME_CHANGED -> callback?.onTimeChanged()
                Intent.ACTION_TIMEZONE_CHANGED -> callback?.onTimeZoneChanged()
            }
        }
    }
}