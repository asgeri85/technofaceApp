package com.example.technofaceapp.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Customer(
    val id: Int? = null,
    val ad: String? = null,
    val durum: String? = null,
    val deger: String? = null,
)