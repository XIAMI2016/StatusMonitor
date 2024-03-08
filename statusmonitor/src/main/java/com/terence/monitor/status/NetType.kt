package com.terence.monitor.status

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
object NetType {
    const val None      = 0x0100
    const val UnKnown   = 0x0200
    const val Mobile    = 0x0400
    const val Ethernet  = 0x0800
    const val WiFi      = 0x1000

    // mobile subjects
    const val Mobile_2G = 0x0401
    const val Mobile_3G = 0x0402
    const val Mobile_4G = 0x0404
    const val Mobile_5G = 0x0408

    fun isMobile(netType: Int) : Boolean {
        val type = netType and Mobile
        return type == Mobile
    }
}