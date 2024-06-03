package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class CancelPaymentResponse(

	@field:SerializedName("status_message")
	val statusMessage: String,

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("fraud_status")
	val fraudStatus: String,

	@field:SerializedName("payment_type")
	val paymentType: String,

	@field:SerializedName("transaction_status")
	val transactionStatus: String,

	@field:SerializedName("status_code")
	val statusCode: String,

	@field:SerializedName("transaction_time")
	val transactionTime: String,

	@field:SerializedName("currency")
	val currency: String,

	@field:SerializedName("merchant_id")
	val merchantId: String,

	@field:SerializedName("gross_amount")
	val grossAmount: String,

	@field:SerializedName("acquirer")
	val acquirer: String,

	@field:SerializedName("order_id")
	val orderId: String
)
