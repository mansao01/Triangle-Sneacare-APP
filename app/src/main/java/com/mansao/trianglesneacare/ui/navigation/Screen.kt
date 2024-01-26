package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
//    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object ProfileEdit : Screen("profileEdit")
    object Main :Screen("main")

//    admin
    object AdminHome : Screen("adminHome")
    object DriverManagement : Screen("driverManagement")
    object DriverRegistration : Screen("driverRegistration")

//    customer
    object CustomerHome : Screen("customerHome")
    object CustomerCart : Screen("customerCart")
    object TransactionList : Screen("transactionList")
    object AddressList : Screen("addressList")
    object AddAddress : Screen("addAddress")


//driver
    object DriverHome : Screen("driverHome")
    object DriverMap : Screen("driverMap")

}