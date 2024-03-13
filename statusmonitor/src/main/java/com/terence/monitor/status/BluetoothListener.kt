package com.terence.monitor.status

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter


/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
internal class BluetoothListener(
        private val context: Context,
        private val onBluetoothStatusCallback: OnBluetoothStatusCallback?=null
) : Listener{

    private val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var receiver : BluetoothStatusReceiver?= null

    override fun registerListener() {
        if(receiver == null)
            receiver = BluetoothStatusReceiver.buildAndRegister(context, onBluetoothStatusCallback)
    }

    override fun unregisterListener() {
        if(receiver != null){
            context.unregisterReceiver(receiver)
            receiver = null
        }
    }

    fun isBtEnable(): Boolean {
        return btManager.adapter.isEnabled
    }

    class BluetoothStatusReceiver(private val callback: OnBluetoothStatusCallback? = null) : BroadcastReceiver() {

        companion object {

            fun buildAndRegister(context: Context, callback: OnBluetoothStatusCallback?) : BluetoothStatusReceiver {
                val receiver = BluetoothStatusReceiver(callback)
                val filter = IntentFilter().apply {
                    addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                    addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                }
                context.registerReceiver(receiver, filter)
                return receiver
            }
        }

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    intent.extras?.let {
                        val state = it.getInt(BluetoothAdapter.EXTRA_STATE)
                        callback?.onBluetoothAdapterStateChanged(state)
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device = intent.getParcelableExtra<BluetoothDevice?>(BluetoothDevice.EXTRA_DEVICE)
                    val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    callback?.onBluetoothDeviceBondStateChanged(state, device)
                }
            }
        }
    }
}