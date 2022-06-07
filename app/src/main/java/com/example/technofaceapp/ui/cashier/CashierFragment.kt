package com.example.technofaceapp.ui.cashier

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.technofaceapp.base.BaseFragment
import com.example.technofaceapp.databinding.FragmentCashierBinding
import com.example.technofaceapp.utils.Constants.CAMERA_REQUEST_CODE

class CashierFragment : BaseFragment<FragmentCashierBinding, CashierViewModel>(
    FragmentCashierBinding::inflate
) {

    private lateinit var codeScanner: CodeScanner
    override val viewModel: CashierViewModel by viewModels<CashierViewModel>()

    override fun createFinished() {
        setupPermissions()
        qrCodeScan()
    }

    override fun observeEvents() {}

    private fun qrCodeScan() {
        codeScanner = CodeScanner(requireContext(), binding.qrScanner)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    findNavController().navigate(CashierFragmentDirections.actionCashierFragmentToVoucherFragment2(
                        it.text))
                }
            }

            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Log.e("Error", it.message.toString())
                }
            }
        }

        binding.qrScanner.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Kamera izni verilmedi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}