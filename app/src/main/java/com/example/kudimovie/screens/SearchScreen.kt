package com.example.kudimovie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kudimovie.presentation.movie.MovieItem
import com.example.kudimovie.components.SearchBar
import com.example.kudimovie.navigation.BottomBar
import com.example.kudimovie.navigation.Graph
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun SearchScreen(navController: NavHostController, viewModel: MainViewModel, onNavigateToDetails: (Int) -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    var searchQuery by remember {
        mutableStateOf("")
    }
    val searchedMovies = viewModel.searchedMovies

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { focusManager.clearFocus() }
    ) {

        SearchBar(focusManager = focusManager, passText = {}, onDone = {
            searchQuery = it
            focusManager.clearFocus()
        })
        if(searchQuery.isNotBlank()){
            LaunchedEffect(searchQuery, viewModel) {
                viewModel.fetchSearched(searchQuery)
            }
            if(!searchedMovies.isNullOrEmpty()){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 30.dp, top = 10.dp, end = 20.dp)
                ) {
                    items(searchedMovies) { movie ->
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
}
