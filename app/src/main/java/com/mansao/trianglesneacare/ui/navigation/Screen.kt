package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
//    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")

    object Main :Screen("main")

//    admin
    object AdminHome : Screen("adminHome")
    object DriverManagement : Screen("driverManagement")

//    customer
    object CustomerHome : Screen("customerHome")
    object CustomerCart : Screen("customerCart")


//driver
    object DriverHome : Screen("driverHome")
    object DriverMap : Screen("driverMap")

}