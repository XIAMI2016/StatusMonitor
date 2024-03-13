package com.terence.monitor.status

import android.content.Context
import android.content.pm.PackageInfo
import android.text.TextUtils
import androidx.annotation.RequiresPermission

/**
 *   Created by Terence.J.Tang on 2024/3/8
 */
internal class PackageChangeMonitor(context: Context) : OnPackageChangeCallback {

    private val packageChangeListener = PackageChangeListener(context, this)
    private val onCallbackMap = mutableMapOf<OnPackageChangeCallback,List<String>>()

    @RequiresPermission(value = "android.permission.QUERY_ALL_PACKAGES")
    fun addCallback(onPackageChangeCallback: OnPackageChangeCallback,packageList: List<String>){
        if(onCallbackMap.containsKey(onPackageChangeCallback)) return
        onCallbackMap.ifEmpty { packageChangeListener.registerListener() }
        onCallbackMap[onPackageChangeCallback] = packageList
    }

    fun removeCallback(onPackageChangeCallback: OnPackageChangeCallback){
        if(!onCallbackMap.containsKey(onPackageChangeCallback)) return
        onCallbackMap.remove(onPackageChangeCallback)
        onCallbackMap.ifEmpty { packageChangeListener.unregisterListener() }
    }

    override fun onPackageAdded(pkg: String, packageInfo: PackageInfo?) {
        onCallbackMap.forEach { (onPackageChangeCallback, packageList) ->
            if(packageList.isEmpty()){
                onPackageChangeCallback.onPackageAdded(pkg, packageInfo)
                return
            }

            val packageName = packageList.find { it == pkg }
            if(!TextUtils.isEmpty(packageName)){
                onPackageChangeCallback.onPackageAdded(pkg, packageInfo)
            }
        }
    }

    override fun onPackageReplaced(pkg: String, packageInfo: PackageInfo?) {
        onCallbackMap.forEach { (onPackageChangeCallback, packageList) ->
            if(packageList.isEmpty()){
                onPackageChangeCallback.onPackageReplaced(pkg, packageInfo)
                return
            }

            val packageName = packageList.find { it == pkg }
            if(!TextUtils.isEmpty(packageName)){
                onPackageChangeCallback.onPackageReplaced(pkg, packageInfo)
            }
        }
    }

    override fun onPackageRemoved(pkg: String) {
        onCallbackMap.forEach { (onPackageChangeCallback, packageList) ->
            if(packageList.isEmpty()){
                onPackageChangeCallback.onPackageRemoved(pkg)
                return
            }

            val packageName = packageList.find { it == pkg }
            if(!TextUtils.isEmpty(packageName)){
                onPackageChangeCallback.onPackageRemoved(pkg)
            }
        }
    }
}