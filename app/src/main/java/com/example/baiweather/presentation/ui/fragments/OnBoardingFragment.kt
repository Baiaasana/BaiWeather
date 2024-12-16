package com.example.baiweather.presentation.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.baiweather.R
import com.example.baiweather.common.AlertDialog
import com.example.baiweather.databinding.FragmentOnBoardingBinding
import com.example.baiweather.presentation.util.openAppSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        binding.btnStart.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToMainFragment())
            } else {
                showAlert()
            }
        }
    }

    private fun showAlert() {
        AlertDialog(title = getString(R.string.required_permissions), description = getString(R.string.allow_app_to_access_your_location), positiveText = getString(
            R.string.go_settings
        ), neutralText = getString(R.string.later)).apply {
            positiveClick = {
                openAppSettings(requireContext())
            }
        }.show(this.parentFragmentManager, getString(R.string.alert))
    }
}