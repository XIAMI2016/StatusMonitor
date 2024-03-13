package com.terence.monitor.status

import android.bluetooth.BluetoothDevice

/**
 *   Created by Terence.J.Tang on 2024/3/13
 */
interface OnBluetoothStatusCallback {

    /**
     * @param state
     * [android.bluetooth.BluetoothAdapter.STATE_OFF]
     * [android.bluetooth.BluetoothAdapter.STATE_ON]
     * [android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF]
     * [android.bluetooth.BluetoothAdapter.STATE_TURNING_ON]
     */
    fun onBluetoothAdapterStateChanged(state : Int)

    /**
     * @param bondState
     * [android.bluetooth.BluetoothDevice.BOND_BONDED]
     * [android.bluetooth.BluetoothDevice.BOND_BONDING]
     * [android.bluetooth.BluetoothDevice.BOND_NONE]
     */
    fun onBluetoothDeviceBondStateChanged(bondState : Int, bluetoothDevice: BluetoothDevice?)
}