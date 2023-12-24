package com.mansao.trianglesneacare.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

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
                screen = Screen.AdminHome.route,
                contentDescription = "Home"
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
                screen = Screen.AdminHome.route,
                contentDescription = "Home"
            ),BottomNavigationItem(
                title = "Cart",
                icon = Icons.Filled.ShoppingCart,
                screen = Screen.CustomerCart.route,
                contentDescription = "Home"
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
