package com.mansao.trianglesneacare.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.section.admin.AdminHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.CustomerHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.HomeViewModel
import com.mansao.trianglesneacare.ui.screen.section.driver.DriverHomeScreen
import com.mansao.trianglesneacare.ui.screen.login.LoginScreen
import com.mansao.trianglesneacare.ui.screen.login.LoginViewModel
import com.mansao.trianglesneacare.ui.screen.register.RegisterScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavHost(navController = navController, startDestination = startDestination) {
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
                navigateToAdminHome = {
                    navController.navigate(Screen.AdminHome.route)
                },
                navigateToCustomerHome = {
                    navController.navigate(Screen.CustomerHome.route)
                },
                navigateToRegister = {
                    navController.navigate(Screen.Register.route)
                })
        }

//        admin ui
        composable(Screen.AdminHome.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            AdminHomeScreen(homeViewModel.uiState)
        }

//        customer ui
        composable(Screen.CustomerHome.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            CustomerHomeScreen(homeViewModel.uiState)
        }
//        driver ui
        composable(Screen.DriverHome.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            DriverHomeScreen(homeViewModel.uiState)
        }

    }
}