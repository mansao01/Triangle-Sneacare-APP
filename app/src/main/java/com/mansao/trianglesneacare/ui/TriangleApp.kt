package com.mansao.trianglesneacare.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriangleApp(
    navController: NavHostController = rememberNavController(),
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavHost(navController = navController, startDestination = Screen.Register.route ){
        composable(Screen.Register.route){

        }
        composable(Screen.Login.route){

        }
        composable(Screen.Home.route){

        }

    }
}