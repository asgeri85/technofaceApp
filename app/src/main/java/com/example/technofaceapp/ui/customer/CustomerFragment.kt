package com.example.technofaceapp.ui.customer

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.technofaceapp.base.BaseFragment
import com.example.technofaceapp.databinding.FragmentCustomerBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class CustomerFragment : BaseFragment<FragmentCustomerBinding, CustomerViewModel>(
    FragmentCustomerBinding::inflate
) {

    override val viewModel: CustomerViewModel by viewModels<CustomerViewModel>()

    override fun createFinished() {
        with(binding) {
            buttonCard.setOnClickListener {
                viewModel.pay("1")
            }
            buttonCash.setOnClickListener {
                viewModel.pay("1")
            }
            buttonCancel.setOnClickListener {
                viewModel.pay("0")
            }
        }
    }

    override fun observeEvents() {
        with(binding) {
            viewModel.customerData.observe(viewLifecycleOwner) {
                it?.let {
                    it.id?.let { it1 -> qrGenerate(it1) }
                    it.deger?.let { it1 -> textAmount.text = "Ödeme Tutarı: $it1" }
                }
            }

            viewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }

            viewModel.error.observe(viewLifecycleOwner) {
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }

            viewModel.isValue.observe(viewLifecycleOwner) {
                if (it) {
                    card.visibility = View.VISIBLE
                    imageView.visibility = View.GONE
                } else {
                    imageView.visibility = View.VISIBLE
                    card.visibility = View.GONE
                }
            }

            viewModel.paymentStatus.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(context, "Teşşekkürler", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "Hata!!! Ödeme yapılamadı", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }
        }

    }

    private fun qrGenerate(id: Int) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(id.toString(), BarcodeFormat.QR_CODE, 512, 512)
            val bmp =
                Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
            for (x in 0 until bitMatrix.width) {
                for (y in 0 until bitMatrix.height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            binding.imageView.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

}