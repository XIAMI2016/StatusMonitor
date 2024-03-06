package com.terence.monitor.status

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
interface OnHotspotChangeCallback {

    fun onDisabled()

    fun onEnabled()

    fun onDisabling()

    fun onEnabling()
}