package com.terence.monitor.status

import android.content.Context

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
interface OnTimeChangeCallback{

    fun onTickMinute()

    fun onTimeChanged()

    fun onTimeZoneChanged()
}