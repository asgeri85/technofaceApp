package com.example.technofaceapp.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.technofaceapp.model.Customer
import com.google.firebase.database.*

class CustomerViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("customers")

    private val _customerData = MutableLiveData<Customer?>()
    val customerData: LiveData<Customer?>
        get() = _customerData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _isValue = MutableLiveData<Boolean>()
    val isValue: LiveData<Boolean>
        get() = _isValue

    private val _paymentStatus = MutableLiveData<Boolean>()
    val paymentStatus: LiveData<Boolean>
        get() = _paymentStatus

    init {
        getCustomerData()
    }

    private fun getCustomerData() {
        _loading.value = true
        ref.child("1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customer = snapshot.getValue(Customer::class.java)

                if (customer != null) {
                    _customerData.value = customer
                    _isValue.value = customer.deger != ""
                    _loading.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
                _loading.value = false
            }
        })
    }

    fun pay(durum: String) {
        _loading.value = true
        val map = HashMap<String, Any>()
        map["durum"] = durum

        ref.child("1").updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                _paymentStatus.value = true
                _loading.value = false
            } else {
                _paymentStatus.value = false
                _loading.value = false
            }
        }
    }

}