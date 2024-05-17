package com.example.kudimovie.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kudimovie.screens.DetailsScreen
import com.example.kudimovie.screens.FavScreen
import com.example.kudimovie.screens.HomeScreen
import com.example.kudimovie.screens.ProfileScreen
import com.example.kudimovie.screens.SearchScreen
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun BottomNavGraph(navController: NavHostController, viewModel: MainViewModel) {

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBar.Home.route
    ) {
        composable(
            route = BottomBar.Home.route
        ) {
            HomeScreen(navController, viewModel = viewModel,
                onNavigateToDetails = {
                    navController.navigate("${Graph.DETAILS}/$it")
                }
            )
        }
        composable(
            route = "${Graph.DETAILS}/{my_param}",
            arguments = listOf(
                navArgument("my_param") {
                    type = NavType.IntType
                }
            )
        ) {
            val param = it.arguments?.getInt("my_param") ?: 0
            DetailsScreen(id = param, viewModel)
        }
        composable(
            route = BottomBar.Favourites.route
        ) {
            FavScreen(navController, viewModel, onNavigateToDetails = {navController.navigate("${Graph.DETAILS}/$it")})
        }
        composable(
            route = BottomBar.Profile.route
        ) {
            ProfileScreen(viewModel)
        }
        composable(
            route = BottomBar.Search.route
        ) {
            SearchScreen(
                navController,
                viewModel,
                onNavigateToDetails = {
                    navController.navigate("${Graph.DETAILS}/$it")
                })
        }
    }
}


object Graph {
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}