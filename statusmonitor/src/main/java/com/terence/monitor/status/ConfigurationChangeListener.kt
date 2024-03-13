package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
internal class ConfigurationChangeListener(
        private val context: Context,
        private val onConfigurationChangeCallback: OnConfigurationChangeCallback
) : Listener{

    private var receiver : ConfigurationBroadcastReceiver?= null

    override fun registerListener() {
        if(receiver == null)
            receiver = ConfigurationBroadcastReceiver.buildAndRegister(context, onConfigurationChangeCallback)
    }

    override fun unregisterListener() {
        if(receiver != null){
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    class ConfigurationBroadcastReceiver(
            private val callback: OnConfigurationChangeCallback?= null
    ) : BroadcastReceiver() {

        companion object {
            fun buildAndRegister(context: Context, callback: OnConfigurationChangeCallback): ConfigurationBroadcastReceiver {
                val receiver = ConfigurationBroadcastReceiver(callback)
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_CONFIGURATION_CHANGED)
                }
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context, intent: Intent) {
            val configuration = context.resources.configuration
            callback?.onConfigurationChanged(configuration)
        }
    }
}