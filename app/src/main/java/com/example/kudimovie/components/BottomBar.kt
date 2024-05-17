package com.example.kudimovie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kudimovie.navigation.BottomBar
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.ui.theme.navbarColor

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBar.Search,
        BottomBar.Home,
        BottomBar.Favourites,
        BottomBar.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route}

    if(bottomBarDestination) {
        NavigationBar(
            containerColor = navbarColor,
            contentColor = bone,
            modifier = Modifier.height(75.dp)
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBar,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        label = { Text( text = screen.title, fontSize = 11.sp ) },
        onClick = { navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        } },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title)
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = bone,
            indicatorColor = navbarColor,
            selectedTextColor = bone,
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray
        )
    )
}
