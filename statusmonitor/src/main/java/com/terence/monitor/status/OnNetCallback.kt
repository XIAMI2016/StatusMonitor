package com.terence.monitor.status

interface OnNetCallback {

    fun onNetAvailable()

    fun onNetLost()

    fun onNetTypeChanged(old : NetType, new : NetType)
}