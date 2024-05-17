package com.example.kudimovie.presentation.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.kudimovie.navigation.Graph
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun MoviePlayingList(
    navController: NavHostController,
    viewModel: MainViewModel,
    onNavigateToDetails: (Int) -> Unit
) {
    val nowPlaying = viewModel.nowPlaying
    LaunchedEffect(viewModel) {
        viewModel.fetchNowPlaying()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        Text(
            text = "Teraz w kinach",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White
        )

        if(nowPlaying != null) {
            LazyRow() {
                items(nowPlaying) {
                        movie -> MovieItem(movie = movie, key = movie.id) {
                    onNavigateToDetails(it)
                    navController.navigate("${Graph.DETAILS}/$it") {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
                }
            }
        }
    }
}
