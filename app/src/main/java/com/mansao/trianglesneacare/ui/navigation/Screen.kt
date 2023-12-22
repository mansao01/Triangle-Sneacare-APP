package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
//    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")

//    admin
    object AdminMain : Screen("adminMain")

//    customer
    object CustomerMain : Screen("customerMain")


//driver
    object DriverMain : Screen("driverMain")

}