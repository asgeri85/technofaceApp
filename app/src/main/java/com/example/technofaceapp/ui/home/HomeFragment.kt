package com.example.technofaceapp.ui.home

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.technofaceapp.base.BaseFragment
import com.example.technofaceapp.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    override fun createFinished() {

        with(binding) {

            buttonCashier.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCashierFragment())
            }

            buttonCustomer.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCustomerFragment())
            }

            buttonError.setOnClickListener {
                throw RuntimeException("Test Crash")
            }
        }
    }

    override fun observeEvents() {}

}