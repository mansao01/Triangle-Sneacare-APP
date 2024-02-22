package com.mansao.trianglesneacare.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.screen.login.LoginScreen
import com.mansao.trianglesneacare.ui.screen.passwordReset.inputEmail.InputEmailScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterScreen
import com.mansao.trianglesneacare.ui.screen.section.MainScreen
import com.mansao.trianglesneacare.utils.canGoBack

@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    sharedViewModel: SharedViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Register.route) {
            RegisterScreen(
                navigateBack = {
                    if (navController.canGoBack) navController.popBackStack()

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
                },
                navigateToInputEmail = {
                    navController.navigate(Screen.InputEmail.route)
                }
            )
        }

        composable(Screen.InputEmail.route) {
            InputEmailScreen(navigateBack = {
                if (navController.canGoBack) {
                    navController.popBackStack()
                }
            })
        }

        composable(Screen.Main.route) {
            MainScreen(
                navigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login.route)
                },
                sharedViewModel = sharedViewModel
            )
        }
    }
}