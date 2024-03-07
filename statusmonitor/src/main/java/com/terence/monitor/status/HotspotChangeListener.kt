package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
internal class HotspotChangeListener(
    private val context: Context,
    private val callback: OnHotspotChangeCallback? = null
) : Listener{

    private var receiver : HotspotStateReceiver? = null

    override fun registerListener() {
        receiver = HotspotStateReceiver.buildAndRegister(context, callback)
    }

    override fun unregisterListener() {
        if(receiver != null) {
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    @RequiresPermission(value = "android.permission.ACCESS_WIFI_STATE")
    fun isApEnable(context: Context): Boolean {
        try {
            val mWifiManager: WifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method = mWifiManager.javaClass.getMethod("isWifiApEnabled")
            method.isAccessible = true
            return method.invoke(mWifiManager) as Boolean
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    private class HotspotStateReceiver(
        private val callback: OnHotspotChangeCallback? = null) : BroadcastReceiver() {

        companion object {

            const val HOTSPOT_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED"

            fun buildAndRegister(context: Context,callback: OnHotspotChangeCallback? = null) : HotspotStateReceiver{
                val receiver = HotspotStateReceiver(callback)
                val filter = IntentFilter(HOTSPOT_STATE_CHANGE_ACTION)
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra("wifi_state", -1)) {
                10 -> { //disabling
                    callback?.onHotspotDisabling()
                }
                11 -> { //disabled
                    callback?.onHotspotDisabled()
                }
                12 -> { //enabling
                    callback?.onHotspotEnabling()
                }
                13 -> { //enabled
                    callback?.onHotspotEnabled()
                }
                else -> {
                    callback?.onHotspotDisabled()
                }
            }
        }
    }
}