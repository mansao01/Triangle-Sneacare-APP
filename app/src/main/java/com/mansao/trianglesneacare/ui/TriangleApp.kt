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
import com.mansao.trianglesneacare.ui.screen.section.customer.home.HomeViewModel
import com.mansao.trianglesneacare.ui.screen.login.LoginScreen
import com.mansao.trianglesneacare.ui.screen.login.LoginViewModel
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.register.RegisterScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.AdminMainScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.CustomerMainScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.DriverMainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(uiState = profileViewModel.uiState)
        }

//        admin ui
        composable(Screen.AdminMain.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            AdminMainScreen(homeViewModel.uiState)
        }

//        customer ui
        composable(Screen.CustomerMain.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            CustomerMainScreen(homeViewModel.uiState)
        }
//        driver ui
        composable(Screen.DriverMain.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            DriverMainScreen(homeViewModel.uiState)
        }

    }
}