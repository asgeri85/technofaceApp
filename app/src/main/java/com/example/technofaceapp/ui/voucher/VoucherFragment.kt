package com.example.technofaceapp.ui.voucher

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.technofaceapp.R
import com.example.technofaceapp.base.BaseFragment
import com.example.technofaceapp.databinding.CardValueBinding
import com.example.technofaceapp.databinding.FragmentVoucherBinding

class VoucherFragment : BaseFragment<FragmentVoucherBinding, VoucherViewModel>(
    FragmentVoucherBinding::inflate
) {

    private val args: VoucherFragmentArgs by navArgs()
    private lateinit var value: String
    override val viewModel: VoucherViewModel by viewModels<VoucherViewModel>()

    override fun createFinished() {
        value = "0"
        selectedValue()
        binding.buttonSend.setOnClickListener {
            args.id?.let { it1 -> viewModel.updateValue(value, it1) }
        }
        binding.textViewValue.text = "Değer : $value"
        binding.buttonNew.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    override fun observeEvents() {
        with(binding) {
            viewModel.paymentStatus.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(context, "İstek gönderildi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    progressBar2.visibility = View.VISIBLE
                } else {
                    progressBar2.visibility = View.GONE
                }
            }

            viewModel.status.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(context,
                        "Müşteri ödeme yapmıştır.Ödeme Bilgisi: Kredi Kartı/Nakit",
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Müşteri siparişi iptal etmiştir", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun selectedValue() {
        with(binding) {
            radio100.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    value = "100"
                    binding.textViewValue.text = "Değer : $value"
                }
            }

            radio200.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    value = "200"
                    binding.textViewValue.text = "Değer : $value"
                }
            }

            radio50.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    value = "50"
                    binding.textViewValue.text = "Değer : $value"
                }
            }

            radioOther.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    alertShow()
                }
            }
        }
    }

    private fun alertShow() {
        val layout = CardValueBinding.inflate(layoutInflater)
        val ad = AlertDialog.Builder(requireContext())

        ad.setTitle("Değer giriniz")
        ad.setView(layout.root)

        ad.setPositiveButton("Tamam") { _, _ ->
            val amount = layout.editCardValue.text.toString().trim()
            if (amount.toInt() > 200) {
                value = amount
                binding.textViewValue.text = "Değer : $value"
            } else {
                Toast.makeText(context, "Miktar 200den büyük olmalı", Toast.LENGTH_SHORT).show()
            }
        }

        ad.setNegativeButton("İptal") { _, _ -> }

        ad.create().show()
    }


}