package com.mansao.trianglesneacare.data.network

object ApiConst {
    const val PRODUCTION_BASE_URL =
        "https://triangle-api-dot-coffeebid-capstone.et.r.appspot.com/v1/"
    const val SANDBOX_BASE_URL =
        "https://triangle-api-sandbox-dot-coffeebid-capstone.et.r.appspot.com/v1/"
    const val LOCAL_BASE_URL = "http://192.168.18.162:8080/v1/"
    const val AUTHORIZATION_KEY = "Authorization"
    const val CHECK_SERVER = "test"
    const val LOGIN = "login"
    const val REFRESH_ACCESS_TOKEN = "token"
    const val REGISTER = "register"
    const val REGISTER_DRIVER = "registerDriver"
    const val PROFILE = "profile"
    const val PROFILE_DETAIL = "profile/detail"
    const val SEND_RESET_PASSWORD = "sendResetPassword"
    const val VERIFY_OTP = "verifyOtp"
    const val RESET_PASSWORD = "resetPassword"
    const val LOGOUT = "logout"
    const val GET_CUSTOMER_ADDRESS = "getCustomerAddress"
    const val GET_DETAIL_CUSTOMER_ADDRESS = "customerAddress/details"
    const val AUTO_COMPLETE_ADDRESS = "autoCompleteAddress"
    const val CREATE_CUSTOMER_ADDRESS = "createCustomerAddress"
    const val DELETE_CUSTOMER_ADDRESS = "customerAddress/delete"
    const val UPDATE_CUSTOMER_ADDRESS = "customerAddress/update"
    const val GEOCODING_ADDRESS = "geoCodeWithAddress"
    const val GEOCODING_PLACE_ID = "geoCodeWithPlaceId"
    const val CALCULATE_DISTANCE = "calculateDistance"
    //    service
    const val CREATE_NEW_CATEGORY = "addCategory"
    const val GET_CATEGORIES = "categories"
    const val CREATE_NEW_SERVICE = "addService"
    const val DELETE_SERVICE = "deleteService/{id}"
    const val GET_SERVICES_BY_CATEGORY = "servicesByCategory"
    const val UPDATE_SERVICE = "updateService"
    const val UPDATE_WASH_STATUS = "updateWashStatus"
    //    customer
    const val CREATE_ORDER = "addOrder"
    const val DELETE_ORDER = "deleteOrder/{orderId}"
    const val ADD_TO_CART = "addToCart"
    const val CREATE_TRANSACTION = "createTransaction"
    const val GET_TRANSACTION_BY_ID = "transactions/{id}"
    const val GET_ALL_TRANSACTION = "getAllTransactions"
    const val GET_TRANSACTION_BY_MONTH = "getTransactionsByMonth"
    const val GET_TRANSACTION_BY_MONTH_AND_PAYMENT_STATUS = "getTransactionsByMonthAndPaymentStatus"
    const val GET_TRANSACTION_BY_DELIVERY_STATUS = "transactionsByDeliveryStatus"
    const val GET_CART = "getCart/{userId}"
    const val UPDATE_DELIVERY_STATUS_BY_ID = "transaction"
//    payment
    const val CHARGE = "transaction/charge"
    const val GET_PAYMENT_STATUS = "transaction/status"
    const val UPDATE_PAYMENT_STATUS = "transaction/update"
    const val PAYMENT_CANCEL = "transaction/cancel"


}