package com.mansao.trianglesneacare.ui.screen.section

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.AuthViewModel
import com.mansao.trianglesneacare.ui.navigation.BottomNavigationItem
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement.DriverManagementScreen
import com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement.DriverManagementViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion.DriverRegistrationScreen
import com.mansao.trianglesneacare.ui.screen.section.admin.home.AdminHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.cart.CartScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.CustomerHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.home.DriverHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.map.MapScreen

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val role = authViewModel.role.value

    val startDestination = when (role) {
        stringResource(id = R.string.customer) -> Screen.CustomerHome.route
        stringResource(id = R.string.admin) -> Screen.AdminHome.route
        else -> Screen.DriverHome.route
    }


    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController, role = role)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
//                customer
                composable(Screen.CustomerHome.route) {
                    CustomerHomeScreen()
                }
                composable(Screen.CustomerCart.route) {
                    CartScreen()
                }
                composable(Screen.Profile.route) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(uiState = profileViewModel.uiState)
                }


//                admin
                composable(Screen.AdminHome.route) {
                    AdminHomeScreen()
                }
                composable(Screen.DriverManagement.route) {
                    val driverManagementViewModel: DriverManagementViewModel = hiltViewModel()
                    DriverManagementScreen(
                        driverManagementViewModel.uiState,
                        navigateToDriverRegistration = {
                            navController.navigate(Screen.DriverRegistration.route)
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(uiState = profileViewModel.uiState)
                }

                composable(Screen.DriverRegistration.route) {
                    DriverRegistrationScreen()
                }

//                driver
                composable(Screen.DriverHome.route) {
                    DriverHomeScreen()
                }

                composable(Screen.DriverMap.route) {
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
fun MainBottomBar(
    navController: NavHostController,
    role: String
) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {

        when (role) {
            stringResource(id = R.string.customer) -> {
                BottomNavigationItem().customerBottomNavigationItem()
                    .forEachIndexed { index, navigationItem ->
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

            stringResource(id = R.string.admin) -> {
                BottomNavigationItem().adminBottomNavigationItem()
                    .forEachIndexed { index, navigationItem ->
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

            stringResource(id = R.string.driver) -> {
                BottomNavigationItem().driverBottomNavigationItem()
                    .forEachIndexed { index, navigationItem ->
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

    }
}

