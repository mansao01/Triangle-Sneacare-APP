package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
    //    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object ProfileEdit : Screen("profileEdit")
    object Main : Screen("main")
    object InputEmail:Screen("inputEmail")
    object OTPVerification:Screen("otpVerification")
    object PasswordChange:Screen("passwordChange")


    //    admin
    object AdminHome : Screen("adminHome")
    object Services : Screen("services")
    object DriverManagement : Screen("driverManagement")
    object DriverRegistration : Screen("driverRegistration")

    //    customer
    object CustomerHome : Screen("customerHome")
    object CustomerCart : Screen("customerCart")
    object TransactionList : Screen("transactionList")
    object AddressList : Screen("addressList")
    object AddAddress : Screen("addAddress")
    object SearchAddress : Screen("searchAddress")
    object UpdateAddress:Screen("updateAddress")
    object Maps : Screen("maps")

    //driver
    object DriverHome : Screen("driverHome")
    object DriverMap : Screen("driverMap")

}