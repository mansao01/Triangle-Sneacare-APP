package com.mansao.trianglesneacare.ui.screen.section

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.ServiceNotAvailable
import com.mansao.trianglesneacare.ui.navigation.BottomNavigationItem
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.BlankScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.profileEdit.ProfileEditScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.address.addAddress.AddAddressScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.address.addressList.AddressListScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.address.searchAddress.SearchAddressScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.address.updateAddress.UpdateAddressScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.cart.CartScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.home.CustomerHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.maps.MapsScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.payment.PaymentScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.payment.checking.PaymentCheckingScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.serviceSelection.ServiceSelectionScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.transaction.createTransaction.CreateTransactionScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.transaction.success.TransactionSuccessScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList.TransactionListScreen
import com.mansao.trianglesneacare.ui.screen.section.customer.uploadImage.UploadImageScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.deliver.DeliverScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.deliver.detail.DeliverDetailScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.pickUp.PickUpScreen
import com.mansao.trianglesneacare.ui.screen.section.driver.pickUp.detail.PickUpDetailScreen
import com.mansao.trianglesneacare.ui.screen.section.owner.home.OwnerHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.service.categories.CategoriesScreen
import com.mansao.trianglesneacare.ui.screen.section.service.categories.add.AddCategoryScreen
import com.mansao.trianglesneacare.ui.screen.section.service.driverRegistrarion.DriverRegistrationScreen
import com.mansao.trianglesneacare.ui.screen.section.service.home.ServiceHomeScreen
import com.mansao.trianglesneacare.ui.screen.section.service.services.ServicesScreen
import com.mansao.trianglesneacare.ui.screen.section.service.services.add.AddServiceScreen
import com.mansao.trianglesneacare.ui.screen.section.service.services.update.UpdateServiceScreen
import com.mansao.trianglesneacare.ui.screen.section.service.transaction.DetailTransactionScreen
import com.mansao.trianglesneacare.utils.ConnectionStatus
import com.mansao.trianglesneacare.utils.NetworkHelper.currentConnectivityStatus
import com.mansao.trianglesneacare.utils.NetworkHelper.observeConnectivityAsFlow
import com.mansao.trianglesneacare.utils.canGoBack

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToLogin: () -> Unit
) {
    mainViewModel.refreshTokenExpired.collectAsState(initial = false).value.let { isExpired ->
        if (!isExpired) {
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
                        ServiceNotAvailable(action = { mainViewModel.checkServiceAvailable() })
                    }
                }
            }
        } else {
            navigateToLogin()
            mainViewModel.logout()
            sharedViewModel.changeSessionExpiredState(true)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
        stringResource(id = R.string.service) -> Screen.ServiceHome.route
        stringResource(id = R.string.owner) -> Screen.OwnerHome.route
        stringResource(id = R.string.driver) -> Screen.PickUp.route
        else -> Screen.BlankScreen.route
    }
    if (checkConnectionStatus()) {
        Scaffold(
            bottomBar = {
                if (
                    currentRoute != Screen.DriverRegistration.route &&
                    currentRoute != Screen.SearchAddress.route &&
                    currentRoute != Screen.AddressList.route &&
                    currentRoute != Screen.Maps.route &&
                    currentRoute != Screen.AddAddress.route &&
                    currentRoute != Screen.ProfileEdit.route &&
                    currentRoute != Screen.UpdateAddress.route &&
                    currentRoute != Screen.AddCategory.route &&
                    currentRoute != Screen.Services.route &&
                    currentRoute != Screen.AddService.route &&
                    currentRoute != Screen.ServiceSelection.route &&
                    currentRoute != Screen.TransactionSuccess.route &&
                    currentRoute != Screen.PaymentChecking.route &&
                    currentRoute != Screen.Payment.route &&
                    currentRoute != Screen.PickUpDetail.route &&
                    currentRoute != Screen.DetailTransaction.route &&
                    currentRoute != Screen.DeliverDetail.route &&
                    currentRoute != Screen.CreateTransaction.route &&
                    currentRoute != Screen.UploadImage.route
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

                    composable(Screen.BlankScreen.route) {
                        BlankScreen()
                    }
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
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() }
                        )
                    }

//                customer
                    composable(Screen.CustomerHome.route) {
                        CustomerHomeScreen(
                            navigateToServiceSelection = { navController.navigate(Screen.ServiceSelection.route) },
                            sharedViewModel = sharedViewModel
                        )
                    }
                    composable(Screen.ServiceSelection.route) {
                        ServiceSelectionScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() },
                            navigateToUploadImage = { navController.navigate(Screen.UploadImage.route) }
                        )
                    }
                    composable(Screen.UploadImage.route) {
                        UploadImageScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() },
                            navigateToHome = {
                                navController.popBackStack(Screen.CustomerHome.route, false)

                            })
                    }

                    composable(Screen.CustomerCart.route) {
                        CartScreen(
                            navigateToCreateTransaction = {
                                navController.navigate(Screen.CreateTransaction.route)
                            },
                            sharedViewModel = sharedViewModel
                        )
                    }

                    composable(Screen.CreateTransaction.route) {
                        CreateTransactionScreen(
                            navigateToAddAddress = { navController.navigate(Screen.AddressList.route) },
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() },
                            navigateToPaymentChecking = {
                                navController.navigate(Screen.PaymentChecking.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            navigateToTransactionSuccess = { navController.navigate(Screen.TransactionSuccess.route) }
                        )
                    }

                    composable(Screen.PaymentChecking.route) {
                        PaymentCheckingScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToPayment = {
                                navController.navigate(Screen.Payment.route)
                            },
                            navigateToPaymentSuccess = {
                                navController.navigate(Screen.TransactionSuccess.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                        inclusive = true

                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            navigateToTransactionList = {
                                navController.navigate(Screen.TransactionList.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }


                    composable(Screen.Payment.route) {
                        PaymentScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() },
                        )
                    }

                    composable(Screen.TransactionSuccess.route) {
                        TransactionSuccessScreen(

                            navigateToTransactionList = {
                                navController.navigate(Screen.TransactionList.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
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
                                navController.navigate(Screen.UpdateAddress.route)
                            },
                            sharedViewModel = sharedViewModel
                        )
                    }

                    composable(Screen.AddAddress.route) {
                        AddAddressScreen(
                            navigateBack = {
                                if (navController.canGoBack) navController.popBackStack()
                            },
                            sharedViewModel = sharedViewModel,
                            navigateToListAddress = {
                                if (navController.canGoBack) {
                                    navController.popBackStack(Screen.AddressList.route, false)
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

                    composable(Screen.UpdateAddress.route) {
                        UpdateAddressScreen(sharedViewModel = sharedViewModel, navigateBack = {
                            if (navController.canGoBack) {
                                navController.popBackStack()
                            }
                        })
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

//                service section
                    composable(Screen.ServiceHome.route) {
                        ServiceHomeScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToDetail = { navController.navigate(Screen.DetailTransaction.route) },
                        )
                    }


                    composable(Screen.DetailTransaction.route) {
                        DetailTransactionScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() })
                    }
                    composable(Screen.Categories.route) {
                        CategoriesScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToServiceList = {
                                navController.navigate(Screen.Services.route)
                            },
                            navigateToAddCategory = { navController.navigate(Screen.AddCategory.route) }
                        )
                    }
                    composable(Screen.AddCategory.route) {
                        AddCategoryScreen(navigateBack = { if (navController.canGoBack) navController.popBackStack() })
                    }
                    composable(Screen.Services.route) {
                        ServicesScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToAddService = {
                                navController.navigate(Screen.AddService.route)
                            },
                            navigateToUpdateService = { navController.navigate(Screen.UpdateService.route) },
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() }
                        )
                    }

                    composable(Screen.AddService.route) {
                        AddServiceScreen(
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() },
                            sharedViewModel = sharedViewModel
                        )
                    }
                    composable(Screen.UpdateService.route) {
                        UpdateServiceScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() })
                    }


                    composable(Screen.DriverRegistration.route) {
                        DriverRegistrationScreen(
                            navigateToDriverManagement = {
                                navController.popBackStack()
                                navController.navigate(Screen.DriverManagement.route)
                            })
                    }

//                driver
                    composable(Screen.PickUp.route) {
                        PickUpScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToDetailPickUp = { navController.navigate(Screen.PickUpDetail.route) }
                        )
                    }
                    composable(Screen.PickUpDetail.route) {
                        PickUpDetailScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { if (navController.canGoBack) navController.popBackStack() })
                    }

                    composable(Screen.Deliver.route) {
                        DeliverScreen(
                            sharedViewModel = sharedViewModel,
                            navigateToDetailDeliver = { navController.navigate(Screen.DeliverDetail.route) })
                    }

                    composable(Screen.DeliverDetail.route) {
                        DeliverDetailScreen(
                            sharedViewModel = sharedViewModel,
                            navigateBack = { navController.popBackStack() })
                    }
//                owner
                    composable(Screen.OwnerHome.route) {
                        OwnerHomeScreen()
                    }

                }
            }
        }
    }else{
        ServiceNotAvailable {

        }
    }
}

@Composable
fun checkConnectionStatus():Boolean {
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    return isConnected
}

@Composable
fun connectivityStatus(): State<ConnectionStatus> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityStatus) {
        context.observeConnectivityAsFlow().collect { value = it }
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
                                        inclusive = true

                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
            }

            stringResource(id = R.string.service) -> {
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

            stringResource(id = R.string.owner) -> {
                BottomNavigationItem().ownerBottomNavigationItem()
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
                            }
                        )
                    }
            }
        }
    }
}

