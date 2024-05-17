package com.example.kudimovie.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kudimovie.navigation.BottomBar
import com.example.kudimovie.navigation.Graph
import com.example.kudimovie.presentation.movie.MovieItem
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun FavScreen(navController: NavHostController, viewModel: MainViewModel, onNavigateToDetails: (Int) -> Unit) {
    val movieList = viewModel.favouriteMovies

    LaunchedEffect(viewModel) {
        viewModel.getFavoriteMovies()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ulubione Filmy",
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 18.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 20.dp)
        ) {
            if (movieList != null) {
                items(movieList.asReversed()) { movie ->
                    MovieItem(movie = movie, key = movie.id) {
                        onNavigateToDetails(it)
                        navController.navigate("${Graph.DETAILS}/$it") {
                            launchSingleTop = true
                            popUpTo(BottomBar.Search.route) {
                                saveState = true
                            }
                        }
                    }
                }
            }
        }
    }
}