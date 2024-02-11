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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.AuthViewModel
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.ServiceNotAvailable
import com.mansao.trianglesneacare.ui.navigation.BottomNavigationItem
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.profileEdit.ProfileEditScreen
import com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement.DriverManagementScreen
import com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement.DriverManagementViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion.DriverRegistrationScreen
import com.mansao.trianglesneacare.ui.screen.section.admin.home.AdminHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.addAddress.AddAddressScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.addressList.AddressListScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.cart.CartScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.CustomerHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.maps.MapsScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.searchAddress.SearchAddressScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.transactionList.TransactionListScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.home.DriverHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.map.MapScreen
import com.mansao.trianglesneacare.utils.canGoBack

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = viewModel()
) {

    mainViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Standby -> {}
            is UiState.Loading -> LoadingDialog()
            is UiState.Success -> MainScreenContent(
                authViewModel = authViewModel,
                navController = navController,
                sharedViewModel = sharedViewModel
            )

            is UiState.Error -> {
                ServiceNotAvailable(action = { mainViewModel.checkServerRunning() })
            }
        }
    }
}

@Composable
fun MainScreenContent(
    authViewModel: AuthViewModel,
    sharedViewModel: SharedViewModel,
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
            if (
                currentRoute != Screen.DriverRegistration.route &&
                currentRoute != Screen.SearchAddress.route &&
                currentRoute != Screen.AddressList.route &&
                currentRoute != Screen.Maps.route &&
                currentRoute != Screen.AddAddress.route
            ) {
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
                composable(Screen.Profile.route) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    ProfileScreen(
                        uiState = profileViewModel.uiState,
                        navigateToProfileEdit = { navController.navigate(Screen.ProfileEdit.route) },
                        navigateToAddressList = { navController.navigate(Screen.AddressList.route) }
                    )
                }

                composable(Screen.ProfileEdit.route) {
                    ProfileEditScreen(
                        navigateBack = {}
                    )
                }

//                customer
                composable(Screen.CustomerHome.route) {
                    CustomerHomeScreen()
                }
                composable(Screen.CustomerCart.route) {
                    CartScreen()
                }
                composable(Screen.TransactionList.route) {
                    TransactionListScreen()
                }
                composable(Screen.AddressList.route) {
                    AddressListScreen(
                        navigateBack = {
                            if (navController.canGoBack) navController.popBackStack()

                        },
                        navigateToSearchAddress = {
                            navController.navigate(Screen.SearchAddress.route)
                        }, navigateToEditAddress = {
                            navController.navigate(Screen.EditAddress.route)
                        })
                }

                composable(Screen.AddAddress.route) {
                    AddAddressScreen(
                        navigateBack = {
                            if (navController.canGoBack) navController.popBackStack()
                        },
                        sharedViewModel = sharedViewModel,
                        navigateToListAddress = {
                            if (navController.canGoBack) {
//                                back to list address trough several screen
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.popBackStack()
                            }

                        }
                    )
                }
                composable(Screen.SearchAddress.route) {
                    SearchAddressScreen(
                        navigateBack = {
                            if (navController.canGoBack) navController.popBackStack()
                        },
                        navigateToMap = {
                            navController.navigate(Screen.Maps.route)
                        },
                        sharedViewModel = sharedViewModel
                    )
                }

                composable(Screen.EditAddress.route){

                }

                composable(Screen.Maps.route) {
                    MapsScreen(
                        sharedViewModel = sharedViewModel,
                        navigateBack = {
                            if (navController.canGoBack) navController.popBackStack()

                        },
                        navigateToAddAddress = {
                            navController.navigate(Screen.AddAddress.route)
                        },
                    )
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
        }

    }
}

