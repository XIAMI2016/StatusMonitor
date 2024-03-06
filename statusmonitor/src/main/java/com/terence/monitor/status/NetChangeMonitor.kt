package com.terence.monitor.status

import android.content.Context
import androidx.annotation.RequiresPermission

internal class NetChangeMonitor(context: Context) : OnNetCallback{

    private val netChangeListener = NetChangeListener(context,this)
    private val onCallbackList = mutableListOf<OnNetCallback>()

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun addCallback(onNetCallback: OnNetCallback){
        onCallbackList.ifEmpty { netChangeListener.registerListener() }

        if (netChangeListener.isAvailable()) {
            onNetCallback.onAvailable()
        } else {
            onNetCallback.onLost()
        }

        onNetCallback.onNetworkTypeChanged(NetType.None,getCurrentNetType())
        onCallbackList.add(onNetCallback)
    }

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun getCurrentNetType() = netChangeListener.getCurrentNetworkType()

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun isAvailable() = netChangeListener.isAvailable()

    fun removeCallback(onNetCallback: OnNetCallback){
        onCallbackList.remove(onNetCallback)
        onCallbackList.ifEmpty { netChangeListener.unregisterListener() }
    }

    override fun onAvailable() {
        onCallbackList.forEach { it.onAvailable() }
    }

    override fun onLost() {
        onCallbackList.forEach { it.onLost() }
    }

    override fun onNetworkTypeChanged(old: NetType, new: NetType) {
        onCallbackList.forEach { it.onNetworkTypeChanged(old,new) }
    }
}