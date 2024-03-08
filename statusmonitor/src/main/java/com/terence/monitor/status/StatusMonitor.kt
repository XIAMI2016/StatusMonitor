package com.terence.monitor.status

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.BatteryManager
import androidx.annotation.RequiresPermission

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
@SuppressLint("StaticFieldLeak")
object StatusMonitor {

    private var app: Context? = null
    private var netChangeMonitor: NetChangeMonitor?= null
    private var hotspotChangeMonitor: HotspotChangeChangeMonitor?= null
    private var batteryStatusMonitor: BatteryStatusMonitor?= null

    fun bind(context: Context){
        if(app == null) app = context.applicationContext
    }

    private fun initNetChangeMonitor() {
        if(netChangeMonitor == null)
            netChangeMonitor = NetChangeMonitor(app!!)
    }

    private fun initHotspotChangeMonitor() {
        if(hotspotChangeMonitor == null)
            hotspotChangeMonitor = HotspotChangeChangeMonitor(app!!)
    }

    private fun initBatteryStatusMonitor() {
        if(batteryStatusMonitor == null)
            batteryStatusMonitor = BatteryStatusMonitor(app!!)
    }

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun addNetChangeCallback(onNetCallback: OnNetCallback){
        initNetChangeMonitor()
        netChangeMonitor!!.addCallback(onNetCallback)
    }

    fun removeNetChangeCallback(onNetCallback: OnNetCallback){
        initNetChangeMonitor()
        netChangeMonitor!!.removeCallback(onNetCallback)
    }

    /**
     * @return [NetType]
     */
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun getCurrentNetType(): Int {
        initNetChangeMonitor()
        return netChangeMonitor!!.getCurrentNetType()
    }

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun isNetAvailable(): Boolean {
        initNetChangeMonitor()
        return netChangeMonitor!!.isAvailable()
    }

    @RequiresPermission(value = "android.permission.ACCESS_WIFI_STATE")
    fun addHotspotCallback(onHotspotCallback: OnHotspotChangeCallback){
        initHotspotChangeMonitor()
        hotspotChangeMonitor!!.addCallbackList(onHotspotCallback)
    }

    fun removeHotspotCallback(onHotspotChangeCallback: OnHotspotChangeCallback){
        initHotspotChangeMonitor()
        hotspotChangeMonitor!!.removeCallbackList(onHotspotChangeCallback)
    }

    @RequiresPermission(value = "android.permission.ACCESS_WIFI_STATE")
    fun isApEnable(): Boolean {
        initHotspotChangeMonitor()
        return hotspotChangeMonitor!!.isApEnable()
    }

    fun addBatteryStatusCallback(onBatteryStatusCallback: OnBatteryStatusCallback){
        initBatteryStatusMonitor()
        batteryStatusMonitor!!.addCallback(onBatteryStatusCallback)
    }

    fun removeBatteryStatusCallback(onBatteryStatusCallback: OnBatteryStatusCallback){
        initBatteryStatusMonitor()
        batteryStatusMonitor!!.removeCallback(onBatteryStatusCallback)
    }

    fun getBatteryMilliAmpereHour() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getMilliAmpereHour()
    }

    fun getBatteryMaxMilliAmpereHour() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getMaxMilliAmpereHour()
    }

    fun getBatteryMilliVolt() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getMilliVolt()
    }

    fun getBatteryTemperatureCelsius() : Float {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getTemperatureCelsius()
    }

    fun getBatteryChargeStatus() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getChargeStatus()
    }

    fun getBatteryChargeType() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getChargeType()
    }

    fun getBatteryPresent() : Boolean {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getPresent()
    }

    fun isLowBattery() : Boolean {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().isLowBattery()
    }

    fun getBatteryHealth() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getHealth()
    }

    fun getBatteryTechnology() : String {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getTechnology()
    }

    fun getBatteryStatusSmallIcon() : Drawable? {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getBatteryStatusSmallIcon()
    }

    fun getBatteryMaxChargingMicroAmpere() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getMaxChargingMicroAmpere()
    }

    fun getBatteryMaxChargingMicroVolts() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getMaxChargingMicroVolts()
    }

    fun getBatteryChargeCounter() : Int {
        initBatteryStatusMonitor()
        return batteryStatusMonitor!!.getBatteryStatusListener().getChargeCounter()
    }
}


