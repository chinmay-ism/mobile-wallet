package com.mifospay.core.model.entity.payload

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayPayload(
    val transactionDate: String,
    val transactionAmount: Int,
    val note: String,
    val dateFormat: String,
    val paymentTypeId: Int = 1,
    val locale: String = "en"
) : Parcelable
