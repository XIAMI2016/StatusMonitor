package com.terence.monitor.status

import android.content.res.Configuration

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
interface OnConfigurationChangeCallback {

    fun onConfigurationChanged(configuration: Configuration)
}