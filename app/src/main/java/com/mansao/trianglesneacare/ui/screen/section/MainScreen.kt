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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.AuthViewModel
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.components.ServiceNotAvailable
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
    authViewModel: AuthViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    mainViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Standby -> {}
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> MainScreenContent(
                authViewModel = authViewModel,
                navController = navController
            )

            is UiState.Error -> {
                ServiceNotAvailable()
            }
        }
    }
}

@Composable
fun MainScreenContent(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val role = authViewModel.role.value

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val startDestination = when (role) {
        stringResource(id = R.string.customer) -> Screen.CustomerHome.route
        stringResource(id = R.string.admin) -> Screen.AdminHome.route
        else -> Screen.DriverHome.route
    }
    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DriverRegistration.route) {
                MainBottomBar(
                    navController = navController,
                    role = role,
                    currentRoute = currentRoute
                )
            }
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
                    DriverRegistrationScreen(
                        navigateToDriverManagement = {
                            navController.popBackStack()
                            navController.navigate(Screen.DriverManagement.route)
                        })
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
    role: String,
    currentRoute: String?
) {
    NavigationBar {

        when (role) {
            stringResource(id = R.string.customer) -> {
                BottomNavigationItem().customerBottomNavigationItem()
                    .forEach {  navigationItem ->
                        val isSelected = currentRoute == navigationItem.screen
                        NavigationBarItem(
                            selected = isSelected,
                            label = {
                                Text(text = navigationItem.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = navigationItem.icon,
                                    contentDescription = navigationItem.contentDescription
                                )
                            }, onClick = {
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
                    .forEach { navigationItem ->
                        val isSelected = currentRoute == navigationItem.screen
                        NavigationBarItem(
                            selected = isSelected,
                            label = {
                                Text(text = navigationItem.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = navigationItem.icon,
                                    contentDescription = navigationItem.contentDescription
                                )
                            }, onClick = {
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
                    .forEach {  navigationItem ->
                        val isSelected = currentRoute == navigationItem.screen

                        NavigationBarItem(
                            selected = isSelected,
                            label = {
                                Text(text = navigationItem.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = navigationItem.icon,
                                    contentDescription = navigationItem.contentDescription
                                )
                            }, onClick = {
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

