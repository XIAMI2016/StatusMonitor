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
            onNetCallback.onNetAvailable()
        } else {
            onNetCallback.onNetLost()
        }

        onNetCallback.onNetTypeChanged(NetType.None,getCurrentNetType())
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

    override fun onNetAvailable() {
        onCallbackList.forEach { it.onNetAvailable() }
    }

    override fun onNetLost() {
        onCallbackList.forEach { it.onNetLost() }
    }

    override fun onNetTypeChanged(old: NetType, new: NetType) {
        onCallbackList.forEach { it.onNetTypeChanged(old,new) }
    }
}