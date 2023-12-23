package com.mansao.trianglesneacare.ui.screen.section.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.navigation.NavigationItem
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileScreen
import com.mansao.trianglesneacare.ui.screen.profile.ProfileViewModel
import com.mansao.trianglesneacare.ui.screen.section.admin.home.AdminHomeScreen

@Composable
fun AdminMainScreen(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            AdminBottomBar(navController = navController, currentRoute = currentRoute)
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.AdminHome.route,
            ) {
                composable(Screen.AdminHome.route) {
                    AdminHomeScreen()
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
fun AdminBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val navigationItems = listOf(
        NavigationItem(
            title = stringResource(id = R.string.home),
            icon = Icons.Default.Home,
            screen = Screen.AdminHome,
            contentDescription = stringResource(id = R.string.home)
        ),
        NavigationItem(
            title = stringResource(id = R.string.profile),
            icon = Icons.Default.Person,
            screen = Screen.Profile,
            contentDescription = stringResource(id = R.string.profile)
        ),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Adjust the height as needed
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            val selectedColor = MaterialTheme.colorScheme.onPrimary
            val unselectedColor = MaterialTheme.colorScheme.onBackground.copy(0.6f)

            IconButton(
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .background(
                        if (currentRoute == item.screen.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.background
                        },
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription,
                    tint = if (isSelected) selectedColor else unselectedColor
                )
            }
        }
    }

}

