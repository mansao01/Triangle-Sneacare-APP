package com.mansao.trianglesneacare.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object AdminHome : Screen("adminHome")
    object CustomerHome : Screen("customerHome")

    object DriverHome : Screen("DriverHome")



}