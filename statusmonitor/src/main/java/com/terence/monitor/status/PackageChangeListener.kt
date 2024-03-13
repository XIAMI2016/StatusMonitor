package com.terence.monitor.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission


/**
 *   Created by Terence.J.Tang on 2024/3/8
 */
internal class PackageChangeListener(
    private val context: Context,
    private val callback: OnPackageChangeCallback,
) : Listener{

    private var receiver : PackageChangeReceiver? = null

    @RequiresPermission(value = "android.permission.QUERY_ALL_PACKAGES")
    override fun registerListener() {
        receiver = PackageChangeReceiver.buildAndRegister(context, callback)
    }

    override fun unregisterListener() {
        if(receiver != null){
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    class PackageChangeReceiver(
        private val callback: OnPackageChangeCallback? = null,
    ) : BroadcastReceiver() {

        companion object {

            /**
             * must add query all packages permission for android version after 10
             */
            @RequiresPermission(value = "android.permission.QUERY_ALL_PACKAGES")
            fun buildAndRegister(context: Context, callback: OnPackageChangeCallback? = null) : PackageChangeReceiver {
                val receiver = PackageChangeReceiver(callback)
                val filter = IntentFilter().apply {
                    addDataScheme("package")
                    addAction(Intent.ACTION_PACKAGE_ADDED)
                    addAction(Intent.ACTION_PACKAGE_REPLACED)
                    addAction(Intent.ACTION_PACKAGE_REMOVED)
                    addAction("android.intent.action.PACKAGE_INSTALL")
                }

                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                val packageName = intent.data!!.schemeSpecificPart
                callback?.onPackageAdded(packageName,getPackageInfo(context,packageName))
            }
            if (intent.action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                val packageName = intent.data!!.schemeSpecificPart
                callback?.onPackageRemoved(packageName)
            }
            if (intent.action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                val packageName = intent.data!!.schemeSpecificPart
                callback?.onPackageReplaced(packageName,getPackageInfo(context,packageName))
            }
        }

        private fun getPackageInfo(context: Context, pkg : String) : PackageInfo? {
            try {
                val packageManager = context.packageManager
                return packageManager.getPackageInfo(pkg, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return null
        }
    }
}