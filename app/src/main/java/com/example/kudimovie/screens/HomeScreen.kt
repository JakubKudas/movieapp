package com.example.kudimovie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kudimovie.presentation.movie.MoviePlayingList
import com.example.kudimovie.presentation.movie.MoviePopularList
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: MainViewModel, onNavigateToDetails: (Int) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 10.dp)
    ) {
        MoviePlayingList(navController = navController, viewModel = viewModel) {
            onNavigateToDetails(it)
        }
        MoviePopularList(navController = navController, viewModel = viewModel) {
            onNavigateToDetails(it)
        }

    }
}
