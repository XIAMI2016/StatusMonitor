package com.terence.monitor.status

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission

/**
 *   Created by Terence.J.Tang on 2024/2/26
 */
internal class NetChangeListener(
    context: Context,
    callback: OnNetCallback? = null,
) : Listener{

    private var networkCallbackRequest = V24NetworkCallbackRequest(context.applicationContext,callback)

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    override fun registerListener() {
        registerV24()
    }

    override fun unregisterListener() {
        unregisterV24()
    }

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun isAvailable() = networkCallbackRequest.isNetAvailable()

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    fun getCurrentNetworkType() = networkCallbackRequest.getNetworkType()

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    private fun registerV24(){
        networkCallbackRequest.registerCallback()
    }

    private fun unregisterV24(){
        networkCallbackRequest.unregisterCallback()
    }

    class V24NetworkCallbackRequest(
        context: Context,
        private val callback: OnNetCallback? = null,
    ) : NetworkCallback(){

        private var currentNetType = NetType.None
        private val mainHandler = android.os.Handler(context.mainLooper)
        private val connectivityManager: ConnectivityManager?
            = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        fun registerCallback(){
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(request, this)
        }

        fun unregisterCallback(){
            connectivityManager?.unregisterNetworkCallback(this)
            mainHandler.removeMessages(0)
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            updateNetworkType()
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            // 网络已连接
            mainHandler.post { callback?.onAvailable() }
            updateNetworkType()
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        override fun onLost(network: Network) {
            super.onLost(network)
            // 网络断开连接
            mainHandler.post { callback?.onLost() }
            updateNetworkType()
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        private fun updateNetworkType(){
            val old = currentNetType
            val new = getNetworkType()
            if(old != new) {
                currentNetType = new
                mainHandler.post { callback?.onNetworkTypeChanged(old, new) }
            }
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        fun isNetAvailableV29(): Boolean {
            val network = connectivityManager?.activeNetwork
            val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        fun isNetAvailable(): Boolean {
            return if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q) {
                isNetAvailableV29()
            } else {
                val network = connectivityManager?.activeNetwork
                val capabilities = connectivityManager?.getNetworkCapabilities(network)
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            }
        }

        @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        fun getNetworkType(): NetType {
            val network = connectivityManager?.activeNetwork
            val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)

            return when {
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> {
                    NetType.Ethernet
                }
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     == true -> {
                    NetType.WiFi
                }
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> {
                    NetType.Mobile
                }
                else -> NetType.None
            }
        }
    }
}