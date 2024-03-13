package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
internal class LanguageChangeListener(
        private val context: Context,
        private val onLanguageChangeCallback: OnLanguageChangeCallback
) : Listener {

    private var receiver : LanguageBroadCast? = null

    override fun registerListener() {
        receiver = LanguageBroadCast.buildAndRegister(context, onLanguageChangeCallback)
    }

    override fun unregisterListener() {
        if(receiver != null){
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    class LanguageBroadCast(private val callback: OnLanguageChangeCallback?= null) : BroadcastReceiver() {

        companion object {

            fun buildAndRegister(context: Context,callback: OnLanguageChangeCallback? = null) : LanguageBroadCast {
                val receiver = LanguageBroadCast(callback)
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_LOCALE_CHANGED)
                }
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            callback?.onLanguageChanged()
        }
    }

}