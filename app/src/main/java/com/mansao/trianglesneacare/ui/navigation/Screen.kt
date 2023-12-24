package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
//    common
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")

//    admin
    object AdminMain : Screen("adminMain")
    object AdminHome : Screen("adminHome")

//    customer
    object CustomerMain : Screen("customerMain")
    object CustomerCart : Screen("customerCart")


//driver
    object DriverMain : Screen("driverMain")

}