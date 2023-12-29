package com.mansao.trianglesneacare.ui.screen.section.customer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.ui.navigation.BottomNavigationItem
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.section.customer.cart.CartScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.CustomerHomeScreen

@Composable
fun CustomerMainScreen(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            CustomerBottomBar(navController = navController)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.CustomerHome.route,
            ) {
                composable(Screen.CustomerHome.route) {
                    CustomerHomeScreen()
                }
                composable(Screen.CustomerCart.route){
                    CartScreen()
                }
                composable(Screen.Profile.route) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(uiState = profileViewModel.uiState)
                }
            }
        }
    }
}

@Composable
fun CustomerBottomBar(
    navController: NavHostController,
) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    NavigationBar {
        BottomNavigationItem().customerBottomNavigationItem().forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                label = {
                    Text(text = navigationItem.title)
                },
                icon = {
                    Icon(
                        imageVector = navigationItem.icon,
                        contentDescription = navigationItem.contentDescription
                    )
                }, onClick = {
                    navigationSelectedItem = index
                    navController.navigate(navigationItem.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }

}
