package com.terence.monitor.status

interface OnNetCallback {

    fun onAvailable()

    fun onLost()

    fun onNetworkTypeChanged(old : NetType, new : NetType)
}