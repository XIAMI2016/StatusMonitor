package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


/**
 *   Created by Terence.J.Tang on 2024/3/5
 */
internal class BatteryStatusListener(
    private val context: Context,
    private val callback: OnBatteryStatusCallback? = null,
) : Listener {

    private var receiver : BatteryStatusReceiver? = null

    override fun registerListener() {
        receiver = BatteryStatusReceiver.buildAndRegister(context, callback)
    }

    override fun unregisterListener() {
        if(receiver != null){
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    fun getMilliAmpereHour() : Int {
        return receiver?.getMilliAmpereHour()?: -1
    }

    fun getMaxMilliAmpereHour() : Int {
        return receiver?.getMaxMilliAmpereHour()?: -1
    }

    fun getMilliVolt() : Int {
        return receiver?.getMilliVolt()?: -1
    }

    fun getTemperatureCelsius() : Float {
        return receiver?.getTemperatureCelsius()?: 0f
    }

    fun getChargeStatus() : Int {
        return receiver?.getChargeStatus()?: BatteryManager.BATTERY_STATUS_UNKNOWN
    }

    fun getChargeType() : Int {
        return receiver?.getChargeType()?: -1
    }

    fun getPresent() : Boolean {
        return receiver?.getBatteryPresent()?: false
    }

    fun isLowBattery() : Boolean = receiver?.isLowBattery()?: false

    fun getHealth() : Int {
        return receiver?.getHealth()?: -1
    }

    fun getTechnology() : String {
        return receiver?.getTechnology() ?: ""
    }

    fun getBatteryStatusSmallIcon() : Drawable? {
        val iconId = receiver?.getBatteryStatusSmallIconId()

        if(iconId != null && iconId != -1) {
            return ContextCompat.getDrawable(context.applicationContext, iconId)
        }

        return null
    }

    fun getMaxChargingMicroAmpere() : Int {
        return receiver?.getMaxChargingMicroAmpere() ?: -1
    }

    fun getMaxChargingMicroVolts() : Int {
        return receiver?.getMaxChargingMicroVolts() ?: -1
    }

    fun getChargeCounter() : Int {
        return  receiver?.getChargeCounter() ?: -1
    }

    class BatteryStatusReceiver(
        private val callback: OnBatteryStatusCallback? = null,
    ) : BroadcastReceiver(){

        companion object {

            private const val DEFAULT_BATTERY_LOW_VALUE = 0.15f

            fun buildAndRegister(context: Context,callback: OnBatteryStatusCallback? = null) : BatteryStatusReceiver {
                val receiver = BatteryStatusReceiver(callback)
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_BATTERY_CHANGED)
                    addAction(Intent.ACTION_BATTERY_LOW)
                }

                val intent = context.registerReceiver(receiver, filter)
                receiver.initCurrentValue(intent)
                return receiver
            }
        }

        private var batteryStatus: Intent?= null
        private var level : Int? = null
        private var scale : Int? = null
        private var volt  : Int? = null
        private var tempera : Float? = null
        private var status : Int? = null
        private var hasSendLowBroadcast = false
        private var lowBatteryThreshold = DEFAULT_BATTERY_LOW_VALUE

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_BATTERY_LOW -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                        callback?.onLowBattery()
                        hasSendLowBroadcast = true
                    }
                }
                Intent.ACTION_BATTERY_CHANGED -> {
                    setCurrentIntent(intent)

                    if(getBatteryPresent()) {
                        val curLevel   = getMilliAmpereHour()
                        val curScale   = getMaxMilliAmpereHour()
                        val curVolt    = getMilliVolt()
                        val curTempera = getTemperatureCelsius()
                        val curStatus  = getChargeStatus()

                        if (level != curLevel || scale != curScale) {
                            callback?.onBatteryLevelChange(curLevel, curScale)
                        }

                        if (volt != curVolt) {
                            callback?.onBatteryVoltageChange(curVolt)
                        }

                        if (tempera != curTempera) {
                            callback?.onBatteryTemperatureChange(curTempera)
                        }

                        if (status != curStatus) {
                            callback?.onChargeStatusChange(curStatus)
                        }

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P
                            && !hasSendLowBroadcast
                            && curLevel / (curScale*1.0f) <= lowBatteryThreshold) {
                            callback?.onLowBattery()
                            hasSendLowBroadcast = true
                        }
                    }
                }
            }
        }

        private fun initCurrentValue(intent: Intent?){
            setCurrentIntent(intent)

            val present = getBatteryPresent()

            if(present) {
                level = getMilliAmpereHour()
                scale = getMaxMilliAmpereHour()
                volt = getMilliVolt()
                tempera = getTemperatureCelsius()
                status = getChargeStatus()
            }
        }

        private fun setCurrentIntent(intent: Intent?){
            batteryStatus = intent
        }

        @RequiresApi(Build.VERSION_CODES.P)
        private fun getBatteryLow() : Boolean {
            return batteryStatus?.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW,false) ?: false
        }

        fun getBatteryPresent(): Boolean {
            return batteryStatus?.getBooleanExtra(BatteryManager.EXTRA_PRESENT,false) ?: false
        }

        fun getChargeStatus() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN) ?: BatteryManager.BATTERY_STATUS_UNKNOWN
        }

        fun getChargeType() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        }

        fun getMilliAmpereHour() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        }

        fun getMaxMilliAmpereHour() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        }

        fun getMilliVolt() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) ?: -1
        }

        fun getTemperatureCelsius() : Float {
            val temp = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
            return temp / 10.0f
        }

        fun isLowBattery() = hasSendLowBroadcast

        fun getHealth() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
        }

        fun getTechnology() : String {
            return batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: ""
        }

        fun getBatteryStatusSmallIconId() : Int {
            return batteryStatus?.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1) ?: -1
        }

        fun getMaxChargingMicroAmpere() : Int {
            return batteryStatus?.getIntExtra("max_charging_current", -1) ?: -1
        }

        fun getMaxChargingMicroVolts() : Int {
            return batteryStatus?.getIntExtra("max_charging_voltage", -1) ?: -1
        }

        fun getChargeCounter() : Int {
            return batteryStatus?.getIntExtra("charge_counter", -1) ?: -1
        }
    }

}