package com.terence.monitor.status

import android.content.Context
import java.util.*

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
internal class LanguageChangeMonitor(context: Context) : OnLanguageChangeCallback {

    private val languageChangeListener = LanguageChangeListener(context, this)
    private val onCallbackList = mutableListOf<OnLanguageChangeCallback>()

    fun addCallback(onLanguageChangeCallback: OnLanguageChangeCallback){
        if(onCallbackList.contains(onLanguageChangeCallback)) return
        onCallbackList.ifEmpty { languageChangeListener.registerListener() }
        onCallbackList.add(onLanguageChangeCallback)
    }

    fun removeCallback(onLanguageChangeCallback: OnLanguageChangeCallback){
        if(!onCallbackList.contains(onLanguageChangeCallback)) return
        onCallbackList.remove(onLanguageChangeCallback)
        onCallbackList.ifEmpty { languageChangeListener.unregisterListener() }
    }

    fun getDefaultLocale() : Locale{
        return Locale.getDefault()
    }

    override fun onLanguageChanged() {
        onCallbackList.forEach { it.onLanguageChanged() }
    }
}