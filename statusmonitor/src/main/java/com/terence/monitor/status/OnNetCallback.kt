package com.terence.monitor.status

interface OnNetCallback {

    fun onNetAvailable()

    fun onNetLost()

    /**
     * @param old [NetType]
     * @param new [NetType]
     */
    fun onNetTypeChanged(old : Int, new : Int)
}