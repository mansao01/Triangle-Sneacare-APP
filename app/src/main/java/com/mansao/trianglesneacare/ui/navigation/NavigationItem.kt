package com.mansao.trianglesneacare.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class BottomNavigationItem(
    val title: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val screen: String = "",
    val contentDescription: String = ""
) {
    fun adminBottomNavigationItem(): List<BottomNavigationItem> {
        return listOf(

            BottomNavigationItem(
                title = "Home",
                icon = Icons.Filled.Home,
                screen = Screen.ServiceHome.route,
                contentDescription = "Home"
            ),
            BottomNavigationItem(
                title = "Services",
                icon = Icons.Filled.LocalLaundryService,
                screen = Screen.Categories.route,
                contentDescription = "Services"
            ),
            BottomNavigationItem(
                title = "Profile",
                icon = Icons.Filled.AccountCircle,
                screen = Screen.Profile.route,
                contentDescription = "Profile"
            ),
        )

    }

    fun customerBottomNavigationItem(): List<BottomNavigationItem> {
        return listOf(

            BottomNavigationItem(
                title = "Home",
                icon = Icons.Filled.Home,
                screen = Screen.CustomerHome.route,
                contentDescription = "Home"
            ),
            BottomNavigationItem(
                title = "Cart",
                icon = Icons.Filled.ShoppingCart,
                screen = Screen.CustomerCart.route,
                contentDescription = "cart"
            ),
            BottomNavigationItem(
                title = "Transaction",
                icon = Icons.Filled.ListAlt,
                screen = Screen.TransactionList.route,
                contentDescription = "transaction"
            ),
            BottomNavigationItem(
                title = "Profile",
                icon = Icons.Filled.AccountCircle,
                screen = Screen.Profile.route,
                contentDescription = "profile"
            ),
        )

    }

    fun ownerBottomNavigationItem(): List<BottomNavigationItem> {
        return listOf(

            BottomNavigationItem(
                title = "Home",
                icon = Icons.Filled.Home,
                screen = Screen.OwnerHome.route,
                contentDescription = "Home"
            ), BottomNavigationItem(
                title = "Service",
                icon = Icons.Filled.LocalLaundryService,
                screen = Screen.Categories.route,
                contentDescription = "Service"
            ),
            BottomNavigationItem(
                title = "Profile",
                icon = Icons.Filled.AccountCircle,
                screen = Screen.Profile.route,
                contentDescription = "Profile"
            )
        )

    }

    fun driverBottomNavigationItem(): List<BottomNavigationItem> {
        return listOf(

            BottomNavigationItem(
                title = "Pick-up",
                icon = Icons.Filled.DeliveryDining,
                screen = Screen.PickUp.route,
                contentDescription = "Pick-up"
            ),

            BottomNavigationItem(
                title = "Deliver",
                icon = Icons.Filled.DeliveryDining,
                screen = Screen.Deliver.route,
                contentDescription = "Deliver"
            ),
            BottomNavigationItem(
                title = "Profile",
                icon = Icons.Filled.AccountCircle,
                screen = Screen.Profile.route,
                contentDescription = "Profile"
            ),
        )

    }
}
