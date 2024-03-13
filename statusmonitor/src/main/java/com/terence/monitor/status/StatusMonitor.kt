package com.terence.monitor.status

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.util.*

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
class StatusMonitor private constructor(context: Context) {

    private var app: Context = context.applicationContext
    private var netChangeMonitor: NetChangeMonitor?= null
    private var hotspotChangeMonitor: HotspotChangeChangeMonitor?= null
    private var batteryStatusMonitor: BatteryStatusMonitor?= null
    private var packageChangeMonitor: PackageChangeMonitor?= null
    private var volumeMonitor : VolumeMonitor?= null
    private var timeChangeMonitor : TimeChangeMonitor?= null
    private val minuteTaskMap = mutableMapOf<MinuteTask,Long>()
    private var languageChangeMonitor : LanguageChangeMonitor?= null
    private var configurationChangeMonitor : ConfigurationChangeMonitor?= null
    private var bluetoothMonitor : BluetoothStatusMonitor?= null

    private var beforeNight = isNightActive()
    private var beforeOrientation = getOrientation()


    /**
     * 分钟计时器
     * 注意：第一分钟可能不到60s
     */
    interface MinuteTask {
        fun onTick(minute: Long)
    }


    companion object {

        @SuppressLint("StaticFieldLeak")
        private var monitor : StatusMonitor? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context) : StatusMonitor {
            if(monitor == null)
                monitor = StatusMonitor(context)
            return monitor!!
        }
    }

    private fun initBluetootMonitor() {
        if (bluetoothMonitor == null)
            bluetoothMonitor = BluetoothStatusMonitor(app)
    }

    private fun initConfigurationMonitor() {
        if(configurationChangeMonitor == null)
            configurationChangeMonitor = ConfigurationChangeMonitor(app)
    }

    private fun initLanguageChangeMonitor() {
        if(languageChangeMonitor == null)
            languageChangeMonitor = LanguageChangeMonitor(app)
    }

    private fun initTimeChangedMonitor() {
        if (timeChangeMonitor == null)
            timeChangeMonitor = TimeChangeMonitor(app)
    }

    private fun initVolumeMonitor() {
        if(volumeMonitor == null)
            volumeMonitor = VolumeMonitor(app)
    }

    private fun initNetChangeMonitor() {
        if(netChangeMonitor == null)
            netChangeMonitor = NetChangeMonitor(app)
    }

    private fun initHotspotChangeMonitor() {
        if(hotspotChangeMonitor == null)
            hotspotChangeMonitor = HotspotChangeChangeMonitor(app)
    }

    private fun initBatteryStatusMonitor() {
        if(batteryStatusMonitor == null)
            batteryStatusMonitor = BatteryStatusMonitor(app)
    }

    private fun initPackageChangeMonitor(){
        if(packageChangeMonitor == null)
            packageChangeMonitor = PackageChangeMonitor(app)
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

    /**
     * @param changeCallback
     * @param packages 指定要监听的包,不指定代表监听所有包
     */
    @RequiresPermission(value = "android.permission.QUERY_ALL_PACKAGES")
    fun addPackageChangeCallback(changeCallback: OnPackageChangeCallback,vararg packages: String){
        initPackageChangeMonitor()
        packageChangeMonitor!!.addCallback(changeCallback, listOf(*packages))
    }

    fun removePackageChangeCallback(changeCallback: OnPackageChangeCallback){
        initPackageChangeMonitor()
        packageChangeMonitor!!.removeCallback(changeCallback)
    }

    fun addVolumeChangeCallback(onVolumeCallback: OnVolumeCallback){
        initVolumeMonitor()
        volumeMonitor!!.addCallback(onVolumeCallback)
    }

    fun removeVolumeChangeCallback(onVolumeCallback: OnVolumeCallback) {
        initVolumeMonitor()
        volumeMonitor!!.removeCallback(onVolumeCallback)
    }

    fun getVolume(streamType : Int) : Int {
        initVolumeMonitor()
        return volumeMonitor!!.getCurrentVolume(streamType)
    }

    fun getMaxVolume(streamType: Int) : Int {
        initVolumeMonitor()
        return volumeMonitor!!.getMaxVolume(streamType)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getMinVolume(streamType: Int) : Int {
        initVolumeMonitor()
        return volumeMonitor!!.getMinVolume(streamType)
    }

    fun addTimeChangeCallback(onTimeChangeCallback: OnTimeChangeCallback) {
        initTimeChangedMonitor()
        return timeChangeMonitor!!.addCallback(onTimeChangeCallback)
    }

    fun removeTimeChangeCallback(onTimeChangeCallback: OnTimeChangeCallback) {
        initTimeChangedMonitor()
        return timeChangeMonitor!!.removeCallback(onTimeChangeCallback)
    }


    private val minuteTimer = object : OnTimeChangeCallback {

        override fun onTickMinute() {
            minuteTaskMap.forEach { (task, beforeTime) ->
                val dtime = ((System.currentTimeMillis() - beforeTime) / (1000 * 60f)).toInt()

                if(dtime > 0){
                    task.onTick(dtime.toLong())
                }
            }
        }

        override fun onTimeChanged() {}

        override fun onTimeZoneChanged() {}
    }

    fun startMinuteTask(minuteTask: MinuteTask){
        if(minuteTaskMap.contains(minuteTask)){
            Log.e("StatusMonitor", "This minuteTask is running")
            return
        }
        minuteTaskMap[minuteTask] = System.currentTimeMillis()
        addTimeChangeCallback(minuteTimer)
    }

    fun stopMinuteTask(minuteTask: MinuteTask){
        if(!minuteTaskMap.contains(minuteTask)) return

        minuteTaskMap.remove(minuteTask)
        if(minuteTaskMap.isEmpty()) removeTimeChangeCallback(minuteTimer)
    }

    fun clearAllMinuteTask(){
        minuteTaskMap.clear()
        removeTimeChangeCallback(minuteTimer)
    }

    fun addLanguageChangeCallback(onLanguageChangeCallback: OnLanguageChangeCallback){
        initLanguageChangeMonitor()
        languageChangeMonitor!!.addCallback(onLanguageChangeCallback)
    }

    fun removeLanguageChangeCallback(onLanguageChangeCallback: OnLanguageChangeCallback){
        initLanguageChangeMonitor()
        languageChangeMonitor!!.removeCallback(onLanguageChangeCallback)
    }

    fun getDefaultLocale() : Locale {
        return languageChangeMonitor!!.getDefaultLocale()
    }

    fun addConfigurationCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
        initConfigurationMonitor()
        configurationChangeMonitor!!.addCallback(onConfigurationChangeCallback)
    }

    fun removeConfigurationCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
        initConfigurationMonitor()
        configurationChangeMonitor!!.removeCallback(onConfigurationChangeCallback)
    }

    fun getConfiguration() : Configuration {
        return app.resources.configuration
    }

    private val customConfigurationChangeCallback = object : OnConfigurationChangeCallback {
        override fun onConfigurationChanged(configuration: Configuration) {
            onUpdateNightMode()
            onUpdateOrientation()
        }
    }

    private val nightModeChangeCallbackList = mutableListOf<OnNightModeChangeCallback>()
    private val orientationCallbackList = mutableListOf<OnOrientationChangeCallback>()

    private fun addCustomConfigurationCallback(){
        if(nightModeChangeCallbackList.isEmpty()){
            addConfigurationCallback(customConfigurationChangeCallback)
        }
    }

    private fun removeCustomConfigurationCallback() {
        if(nightModeChangeCallbackList.isEmpty()){
            removeConfigurationCallback(customConfigurationChangeCallback)
        }
    }

    fun isNightActive() : Boolean {
        return app.resources.getBoolean(R.bool.is_night)
    }

    private fun onUpdateNightMode(){
        val curNight = isNightActive()
        if(beforeNight != curNight){
            nightModeChangeCallbackList.forEach { it.onNightModeChanged(curNight) }
            beforeNight = curNight
        }
    }

    fun addNightModeChangeCallback(onNightModeChangeCallback: OnNightModeChangeCallback){
        if(nightModeChangeCallbackList.contains(onNightModeChangeCallback)) return
        addCustomConfigurationCallback()
        nightModeChangeCallbackList.add(onNightModeChangeCallback)
    }

    fun removeNightModeChangeCallback(onNightModeChangeCallback: OnNightModeChangeCallback){
        if(!nightModeChangeCallbackList.contains(onNightModeChangeCallback)) return
        nightModeChangeCallbackList.remove(onNightModeChangeCallback)
        removeCustomConfigurationCallback()
    }

    fun getOrientation() = getConfiguration().orientation

    private fun onUpdateOrientation(){
        val curOrientation = getOrientation()
        if(beforeOrientation != curOrientation){
            orientationCallbackList.forEach { it.onOrientationChanged(curOrientation) }
            beforeOrientation = curOrientation
        }
    }

    fun addOrientationChangeCallback(onOrientationChangeCallback: OnOrientationChangeCallback){
        if(orientationCallbackList.contains(onOrientationChangeCallback)) return
        addCustomConfigurationCallback()
        orientationCallbackList.add(onOrientationChangeCallback)
    }

    fun removeOrientationChangeCallback(onOrientationChangeCallback: OnOrientationChangeCallback){
        if(!orientationCallbackList.contains(onOrientationChangeCallback)) return
        orientationCallbackList.remove(onOrientationChangeCallback)
        removeCustomConfigurationCallback()
    }

    fun addBluetoothStatusCallback(onBluetoothStatusCallback: OnBluetoothStatusCallback) {
        initBluetootMonitor()
        bluetoothMonitor!!.addCallback(onBluetoothStatusCallback)
    }

    fun removeBluetoothStatusCallback(onBluetoothStatusCallback: OnBluetoothStatusCallback) {
        initBluetootMonitor()
        bluetoothMonitor!!.removeCallback(onBluetoothStatusCallback)
    }
}


