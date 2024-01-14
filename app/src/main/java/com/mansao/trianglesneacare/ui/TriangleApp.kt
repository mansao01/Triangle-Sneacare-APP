package com.mansao.trianglesneacare.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.login.LoginScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterScreen
import com.mansao.trianglesneacare.ui.screen.section.MainScreen

@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Register.route) {
            RegisterScreen(
                navigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToMain = {
                    navController.navigate(Screen.Main.route)
                },
                navigateToRegister = {
                    navController.navigate(Screen.Register.route)
                })
        }

        composable(Screen.Main.route) {
            MainScreen()
        }


    }
}