package com.mansao.trianglesneacare.ui.screen.section.driver

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
import com.mansao.trianglesneacare.ui.screen.section.driver.home.DriverHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.map.MapScreen

@Composable
fun DriverMainScreen(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            DriverBottomBar(navController = navController)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.DriverMain.route,
            ) {
                composable(Screen.DriverHome.route) {
                    DriverHomeScreen()
                }

                composable(Screen.DriverMap.route){
                    MapScreen()
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
fun DriverBottomBar(
    navController: NavHostController,
) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    NavigationBar {
        BottomNavigationItem().driverBottomNavigationItem().forEachIndexed { index, navigationItem ->
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
