package com.terence.statusmonitor

import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.terence.monitor.status.*
import com.terence.statusmonitor.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() , OnNetCallback, OnHotspotChangeCallback, OnBatteryStatusCallback{

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var statusMonitor: StatusMonitor

    private val onVolumeCallback = object : OnVolumeCallback {
        override fun onVolumeChanged(streamType: Int, volume: Int) {
            Log.d("TAG", "onVolumeChanged: streamType = $streamType, volume = $volume")

            Toast.makeText(requireContext(),"volume = $volume",Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusMonitor = StatusMonitor.getInstance(requireContext())
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        statusMonitor.addVolumeChangeCallback(onVolumeCallback)


        statusMonitor.addNetChangeCallback(this)
//        StatusMonitor.addHotspotCallback(this)
//        StatusMonitor.addBatteryStatusCallback(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        statusMonitor.removeNetChangeCallback(this)
//        StatusMonitor.removeHotspotCallback(this)
//        StatusMonitor.removeBatteryStatusCallback(this)
        statusMonitor.removeVolumeChangeCallback(onVolumeCallback)
        _binding = null
    }

    override fun onNetAvailable() {
//        Toast.makeText(requireContext(),"网络已连接，current = ${Thread.currentThread().name}",Toast.LENGTH_LONG).show()
    }

    override fun onNetLost() {
//        Toast.makeText(requireContext(),"网络已断开，current = ${Thread.currentThread().name}",Toast.LENGTH_LONG).show()
    }

    override fun onNetTypeChanged(old: Int, new: Int) {
        Toast.makeText(requireContext(),"网络变化，old = $old,new = $new",Toast.LENGTH_LONG).show()

        Log.d("TAG", "onNetTypeChanged: is Mobile = ${NetType.isMobile(new)}")
        Log.d("TAG", "onNetTypeChanged: is 4G = ${new == NetType.Mobile_4G}")
        Log.d("TAG", "onNetTypeChanged: is 3G = ${new == NetType.Mobile_3G}")
        Log.d("TAG", "onNetTypeChanged: is 2G = ${new == NetType.Mobile_2G}")

    }

    override fun onWifiStateChanged(networkInfo: NetworkInfo?, wifiInfo: WifiInfo) {
        Toast.makeText(requireContext(),"网络变化，wifi = $networkInfo",Toast.LENGTH_LONG).show()
    }

    override fun onHotspotDisabled() {
        Toast.makeText(requireContext(),"F onDisabled", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotEnabled() {
        Toast.makeText(requireContext(),"F onEnabled", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotDisabling() {
        Toast.makeText(requireContext(),"F onDisabling", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotEnabling() {
        Toast.makeText(requireContext(),"F onEnabling", Toast.LENGTH_LONG).show()
    }

    override fun onBatteryLevelChange(curMilliAmpereHour: Int, maxMilliAmpereHour: Int) {
        Log.d("FirstFragment", "F onBatteryLevelChange,curMilliAmpereHour = $curMilliAmpereHour, maxMilliAmpereHour = $maxMilliAmpereHour")
        Toast.makeText(requireContext(),"F onBatteryLevelChange,curMilliAmpereHour = $curMilliAmpereHour, maxMilliAmpereHour = $maxMilliAmpereHour", Toast.LENGTH_LONG).show()
    }

    override fun onBatteryVoltageChange(milliVolt: Int) {
        Log.d("FirstFragment", "F onBatteryVoltageChange = $milliVolt")
        Toast.makeText(requireContext(),"F onBatteryVoltageChange = $milliVolt", Toast.LENGTH_LONG).show()
    }

    override fun onBatteryTemperatureChange(temperatureCelsius: Float) {
        Log.d("FirstFragment", "F onBatteryTemperatureChange = $temperatureCelsius")
        Toast.makeText(requireContext(),"F onBatteryTemperatureChange = $temperatureCelsius", Toast.LENGTH_LONG).show()
    }

    override fun onChargeStatusChange(status: Int) {
        Log.d("FirstFragment", "F onChargedStatusChange = $status")

        Toast.makeText(requireContext(),"F onChargedStatusChange = $status", Toast.LENGTH_LONG).show()
    }

    override fun onLowBattery() {
        Log.d("FirstFragment", "F onBatteryLow")
        Toast.makeText(requireContext(),"F onBatteryLow", Toast.LENGTH_LONG).show()
    }
}