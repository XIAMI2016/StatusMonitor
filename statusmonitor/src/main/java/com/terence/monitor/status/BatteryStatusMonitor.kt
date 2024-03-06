package com.terence.monitor.status

import android.content.Context

/**
 *   Created by Terence.J.Tang on 2024/3/6
 */
internal class BatteryStatusMonitor(context: Context) : OnBatteryStatusCallback {

    private val batteryStatusListener = BatteryStatusListener(context,this)
    private val onCallbackList = mutableListOf<OnBatteryStatusCallback>()

    fun addCallback(onBatteryStatusCallback: OnBatteryStatusCallback){
        onCallbackList.ifEmpty { batteryStatusListener.registerListener() }

        onBatteryStatusCallback.onBatteryVoltageChange(batteryStatusListener.getMilliVolt())
        onBatteryStatusCallback.onBatteryTemperatureChange(batteryStatusListener.getTemperatureCelsius())
        onBatteryStatusCallback.onChargeStatusChange(batteryStatusListener.getChargeStatus())
        onBatteryStatusCallback.onBatteryLevelChange(
            batteryStatusListener.getMilliAmpereHour(),batteryStatusListener.getMaxMilliAmpereHour())

        onCallbackList.add(onBatteryStatusCallback)
    }

    fun removeCallback(onBatteryStatusCallback: OnBatteryStatusCallback){
        onCallbackList.remove(onBatteryStatusCallback)
        onCallbackList.ifEmpty { batteryStatusListener.unregisterListener() }
    }

    fun getBatteryStatusListener() = batteryStatusListener

    override fun onBatteryLevelChange(curMilliAmpereHour: Int, maxMilliAmpereHour: Int) {
        onCallbackList.forEach { it.onBatteryLevelChange(curMilliAmpereHour, maxMilliAmpereHour) }
    }

    override fun onBatteryVoltageChange(milliVolt: Int) {
        onCallbackList.forEach { it.onBatteryVoltageChange(milliVolt) }
    }

    override fun onBatteryTemperatureChange(temperatureCelsius: Float) {
        onCallbackList.forEach { it.onBatteryTemperatureChange(temperatureCelsius) }
    }

    override fun onChargeStatusChange(status: Int) {
        onCallbackList.forEach { it.onChargeStatusChange(status) }
    }

    override fun onLowBattery() {
        onCallbackList.forEach { it.onLowBattery() }
    }

}