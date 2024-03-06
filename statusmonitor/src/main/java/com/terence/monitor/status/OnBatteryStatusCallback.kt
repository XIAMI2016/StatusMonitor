package com.terence.monitor.status

/**
 *   Created by Terence.J.Tang on 2024/3/5
 */
interface OnBatteryStatusCallback {

    fun onBatteryLevelChange(curMilliAmpereHour: Int, maxMilliAmpereHour: Int)

    fun onBatteryVoltageChange(milliVolt: Int)

    fun onBatteryTemperatureChange(temperatureCelsius: Float)

    /**
     * @param status 充电状态
     *
     * [android.os.BatteryManager.BATTERY_STATUS_FULL]
     * [android.os.BatteryManager.BATTERY_STATUS_CHARGING]
     * [android.os.BatteryManager.BATTERY_STATUS_DISCHARGING]
     * [android.os.BatteryManager.BATTERY_STATUS_NOT_CHARGING]
     * [android.os.BatteryManager.BATTERY_STATUS_UNKNOWN]
     */
    fun onChargeStatusChange(status: Int)

    fun onLowBattery()
}