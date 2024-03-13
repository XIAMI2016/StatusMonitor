package com.terence.statusmonitor

import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.terence.monitor.status.*
import com.terence.statusmonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnPackageChangeCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var statusMonitor: StatusMonitor


    private val onLanguageChangeCallback = object : OnLanguageChangeCallback {
        override fun onLanguageChanged() {
            Toast.makeText(this@MainActivity,"当前语言 = ${statusMonitor.getDefaultLocale()}",Toast.LENGTH_LONG).show()
        }
    }

    private val onConfigurationChangeCallback = object : OnConfigurationChangeCallback {
        override fun onConfigurationChanged(configuration: Configuration) {
            Toast.makeText(this@MainActivity,"Configuration = ${statusMonitor.getConfiguration()}",Toast.LENGTH_LONG).show()
        }
    }

    private val onNightModeChangeCallback = object : OnNightModeChangeCallback {
        override fun onNightModeChanged(isNight: Boolean) {
            Toast.makeText(this@MainActivity,"isNight = ${statusMonitor.isNightActive()}",Toast.LENGTH_LONG).show()
        }
    }

    private val onOrientationChangeCallback = object : OnOrientationChangeCallback {

        override fun onOrientationChanged(orientation: Int) {
            Toast.makeText(this@MainActivity,"orientation = ${orientation}",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

        statusMonitor = StatusMonitor.getInstance(this)
//        statusMonitor.addPackageChangeCallback(this)
////        statusMonitor.addLanguageChangeCallback(onLanguageChangeCallback)
//        statusMonitor.addConfigurationCallback(onConfigurationChangeCallback)
//        statusMonitor.addNightModeChangeCallback(onNightModeChangeCallback)
        statusMonitor.addOrientationChangeCallback(onOrientationChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        statusMonitor.clearAllMinuteTask()
        statusMonitor.removeLanguageChangeCallback(onLanguageChangeCallback)
        statusMonitor.removeConfigurationCallback(onConfigurationChangeCallback)
        statusMonitor.removeNightModeChangeCallback(onNightModeChangeCallback)
        statusMonitor.removeOrientationChangeCallback(onOrientationChangeCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPackageAdded(pkg: String, packageInfo: PackageInfo?) {
        Log.d("TAG", "onPackageAdded: info = $packageInfo")
        Toast.makeText(this,"S onPackageAdded = $pkg, info = $packageInfo", Toast.LENGTH_LONG).show()
    }

    override fun onPackageReplaced(pkg: String, packageInfo: PackageInfo?) {
        Log.d("TAG", "onPackageReplaced: info = $packageInfo")
        Toast.makeText(this,"S onPackageReplaced = $pkg, info = $packageInfo", Toast.LENGTH_LONG).show()
    }

    override fun onPackageRemoved(pkg: String) {
        Log.d("TAG", "onPackageRemoved: ")

        Toast.makeText(this,"S onPackageRemoved = $pkg", Toast.LENGTH_LONG).show()
    }
}