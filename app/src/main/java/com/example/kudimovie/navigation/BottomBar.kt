package com.example.kudimovie.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBar(
        "home_screen",
        "Główna",
        Icons.Rounded.Home
    )
    object Favourites: BottomBar(
        "fav_screen",
        "Ulubione",
        Icons.Rounded.Favorite
    )
    object Search: BottomBar(
        "search_screen",
        "Szukaj",
        Icons.Rounded.Search
    )
    object Profile: BottomBar(
        "notification_screen",
        "Profil",
        Icons.Rounded.AccountCircle
    )
}
