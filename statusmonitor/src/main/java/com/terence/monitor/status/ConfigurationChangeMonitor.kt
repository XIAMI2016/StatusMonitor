package com.terence.monitor.status

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.util.Log

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
class ConfigurationChangeMonitor(private val context: Context)  {

    fun addCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
        ConfigurationListenService.addCallback(onConfigurationChangeCallback)

        if(!ConfigurationListenService.isActive) {
            context.startService(Intent(context, ConfigurationListenService::class.java))
        }
    }

    fun removeCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
        ConfigurationListenService.removeCallback(onConfigurationChangeCallback)
    }

    fun closeService(){
        context.stopService(Intent(context, ConfigurationListenService::class.java))
    }

    class ConfigurationListenService : Service(),OnConfigurationChangeCallback {

        companion object {
            private val onCallbackList = mutableListOf<OnConfigurationChangeCallback>()

            var isActive = false

            fun addCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
                if(onCallbackList.contains(onConfigurationChangeCallback)) return
                onCallbackList.add(onConfigurationChangeCallback)
            }

            fun removeCallback(onConfigurationChangeCallback: OnConfigurationChangeCallback){
                if(!onCallbackList.contains(onConfigurationChangeCallback)) return
                onCallbackList.remove(onConfigurationChangeCallback)
            }
        }


        override fun onBind(intent: Intent?): IBinder? = null

        private lateinit var configurationChangeListener : ConfigurationChangeListener

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            return START_NOT_STICKY //杀死后不重启
        }

        override fun onCreate() {
            super.onCreate()
            isActive = true
            configurationChangeListener = ConfigurationChangeListener(this, this)
            configurationChangeListener.registerListener()
        }

        override fun onDestroy() {
            super.onDestroy()
            onCallbackList.clear()
            configurationChangeListener.unregisterListener()
            isActive = false
        }

        override fun onConfigurationChanged(configuration: Configuration) {
            onCallbackList.forEach { it.onConfigurationChanged(configuration) }
        }
    }
}