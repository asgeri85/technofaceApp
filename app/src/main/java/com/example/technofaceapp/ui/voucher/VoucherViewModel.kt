package com.example.technofaceapp.ui.voucher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.technofaceapp.model.Customer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VoucherViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("customers")

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _paymentStatus = MutableLiveData<Boolean>()
    val paymentStatus: LiveData<Boolean>
        get() = _paymentStatus

    private val _customerData = MutableLiveData<Customer?>()
    val customerData: LiveData<Customer?>
        get() = _customerData

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    private fun getCustomerPayment(id: String) {
        ref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customer = snapshot.getValue(Customer::class.java)

                if (customer != null) {
                    _customerData.value = customer
                    if (customer.durum == "0") {
                        _status.value = false
                    } else if (customer.durum == "1") {
                        _status.value = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun updateValue(value: String, id: String) {
        _loading.value = true
        val map = HashMap<String, Any>()
        map["deger"] = value

        ref.child(id).updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                _paymentStatus.value = true
                _loading.value = false
                getCustomerPayment(id)
            } else {
                _paymentStatus.value = false
                _loading.value = true
            }
        }
    }
}