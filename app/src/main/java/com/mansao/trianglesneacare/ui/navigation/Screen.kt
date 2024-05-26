package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
    //    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object ProfileEdit : Screen("profileEdit")
    object Main : Screen("main")
    object InputEmail : Screen("inputEmail")
    object OTPVerification : Screen("otpVerification")
    object PasswordChange : Screen("passwordChange")


    //    service section
    object AdminHome : Screen("adminHome")
    object Categories : Screen("categories")
    object AddCategory : Screen("addCategory")
    object Services : Screen("categories/services")
    object AddService : Screen("categories/services/add")

    object DriverManagement : Screen("driverManagement")
    object DriverRegistration : Screen("driverRegistration")

    //    customer
    object CustomerHome : Screen("customerHome")
    object CustomerCart : Screen("customerCart")
    object TransactionList : Screen("transactionList")
    object AddressList : Screen("addressList")
    object AddAddress : Screen("addAddress")
    object SearchAddress : Screen("searchAddress")
    object UpdateAddress : Screen("updateAddress")
    object Maps : Screen("maps")

    //driver
    object DriverHome : Screen("driverHome")
    object DriverMap : Screen("driverMap")

//    owner

}