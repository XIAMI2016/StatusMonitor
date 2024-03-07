package com.terence.statusmonitor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.terence.monitor.status.OnHotspotChangeCallback
import com.terence.monitor.status.StatusMonitor
import com.terence.statusmonitor.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() , OnHotspotChangeCallback{

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        val available = StatusMonitor.isNetAvailable()
        Toast.makeText(requireContext(),"isNetworkAvailable = $available",Toast.LENGTH_LONG).show()

        StatusMonitor.addHotspotCallback(this)

    }


    override fun onDestroyView() {
        StatusMonitor.removeHotspotCallback(this)
        super.onDestroyView()
        _binding = null
    }

    override fun onHotspotDisabled() {
        Toast.makeText(requireContext(),"S onDisabled", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotEnabled() {
        Toast.makeText(requireContext(),"S onEnabled", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotDisabling() {
        Toast.makeText(requireContext(),"S onDisabling", Toast.LENGTH_LONG).show()
    }

    override fun onHotspotEnabling() {
        Toast.makeText(requireContext(),"S onEnabling", Toast.LENGTH_LONG).show()
    }
}