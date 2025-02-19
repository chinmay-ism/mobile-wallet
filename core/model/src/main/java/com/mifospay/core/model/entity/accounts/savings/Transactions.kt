package com.mifospay.core.model.entity.accounts.savings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transactions(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("transactionType")
    var transactionType: TransactionType = TransactionType(),

    @SerializedName("accountId")
    var accountId: Int? = null,

    @SerializedName("accountNo")
    var accountNo: String? = null,

    @SerializedName("date")
    var date: List<Int?> = ArrayList(),

    @SerializedName("currency")
    var currency: Currency = Currency(),

    @SerializedName("paymentDetailData")
    var paymentDetailData: PaymentDetailData? = null,

    @SerializedName("amount")
    var amount: Double = 0.0,

    @SerializedName("transfer")
    var transfer: Transfer = Transfer(),

    @SerializedName("runningBalance")
    var runningBalance: Double? = null,

    @SerializedName("reversed")
    var reversed: Boolean? = null,

    @SerializedName("submittedOnDate")
    var submittedOnDate: List<Int> = ArrayList(),

    @SerializedName("interestedPostedAsOn")

    var interestedPostedAsOn: Boolean? = null
) : Parcelable {
    constructor() : this(
        0,
        TransactionType(),
        0,
        "",
        ArrayList(),
        Currency(),
        PaymentDetailData(),
        0.0,
        Transfer(),
        0.0,
        false,
        ArrayList(),
        false
    )
}
