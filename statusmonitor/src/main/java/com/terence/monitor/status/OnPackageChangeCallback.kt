package com.terence.monitor.status

import android.content.pm.PackageInfo

/**
 *   Created by Terence.J.Tang on 2024/3/8
 */
interface OnPackageChangeCallback {

    fun onPackageAdded(pkg: String, packageInfo: PackageInfo?)

    fun onPackageReplaced(pkg: String, packageInfo: PackageInfo?)

    fun onPackageRemoved(pkg : String)
}