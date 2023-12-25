package com.mansao.trianglesneacare.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.login.LoginScreen
import com.mansao.trianglesneacare.ui.screen.login.LoginViewModel
import com.mansao.trianglesneacare.ui.screen.register.RegisterScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.AdminMainScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.CustomerMainScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.DriverMainScreen

@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

//        common ui (used each role)
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                uiState = registerViewModel.uiState,
                navigateToLogin = {
                    navController.navigate(Screen.Login.route)
                })
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(uiState = loginViewModel.uiState,
                navigateToAdminMain = {
                    navController.navigate(Screen.AdminMain.route)
                },
                navigateToCustomerMain = {
                    navController.navigate(Screen.CustomerMain.route)
                },
                navigateToDriverMain = {
                    navController.navigate(Screen.DriverMain.route)
                },
                navigateToRegister = {
                    navController.navigate(Screen.Register.route)
                })
        }


//        admin ui
        composable(Screen.AdminMain.route) {
            AdminMainScreen()
        }

//        customer ui
        composable(Screen.CustomerMain.route) {
            CustomerMainScreen()
        }
//        driver ui
        composable(Screen.DriverMain.route) {
            DriverMainScreen()
        }

    }
}