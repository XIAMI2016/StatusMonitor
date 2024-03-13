package com.terence.monitor.status

import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.telephony.AvailableNetworkInfo

interface OnNetCallback {

    fun onNetAvailable()

    fun onNetLost()

    /**
     * @param old [NetType]
     * @param new [NetType]
     */
    fun onNetTypeChanged(old : Int, new : Int)

    fun onWifiStateChanged(networkInfo: NetworkInfo?, wifiInfo: WifiInfo)
}