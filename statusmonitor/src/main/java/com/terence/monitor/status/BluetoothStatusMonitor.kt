package com.terence.monitor.status

import android.bluetooth.BluetoothDevice
import android.content.Context

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
internal class BluetoothStatusMonitor(private val context: Context) : OnBluetoothStatusCallback {

    private val bluetoothListener = BluetoothListener(context, this)
    private val onCallbackList = mutableListOf<OnBluetoothStatusCallback>()

    fun addCallback(onBluetoothStatusCallback: OnBluetoothStatusCallback){
        if(onCallbackList.contains(onBluetoothStatusCallback)) return
        onCallbackList.ifEmpty { bluetoothListener.registerListener() }
        onCallbackList.add(onBluetoothStatusCallback)
    }

    fun removeCallback(onBluetoothStatusCallback: OnBluetoothStatusCallback){
        if(!onCallbackList.contains(onBluetoothStatusCallback)) return
        onCallbackList.remove(onBluetoothStatusCallback)
        onCallbackList.ifEmpty { bluetoothListener.unregisterListener() }
    }

    override fun onBluetoothAdapterStateChanged(state: Int) {
        onCallbackList.forEach { it.onBluetoothAdapterStateChanged(state) }
    }

    override fun onBluetoothDeviceBondStateChanged(bondState: Int, bluetoothDevice: BluetoothDevice?) {
        onCallbackList.forEach { it.onBluetoothDeviceBondStateChanged(bondState, bluetoothDevice) }
    }
}