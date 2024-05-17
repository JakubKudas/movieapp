package com.example.kudimovie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.kudimovie.components.BottomBar
import com.example.kudimovie.navigation.BottomNavGraph
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.network.connectivity.ConnectivityObserver
import com.example.kudimovie.network.connectivity.NetworkConnectivityObserver
import com.example.kudimovie.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val connectivityObserver = NetworkConnectivityObserver(LocalContext.current)
    val networkStatus by rememberUpdatedState(connectivityObserver.observe().collectAsState(
        ConnectivityObserver.Status.Unavailable).value)

    if(networkStatus == ConnectivityObserver.Status.Available) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController) },
            content = {
                    paddingValues ->  Column(modifier = Modifier.padding(paddingValues)
            ) { BottomNavGraph(navController = navController, viewModel = viewModel) }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        )
    } else if(
        networkStatus == ConnectivityObserver.Status.Lost
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(imageVector = Icons.Rounded.Refresh, contentDescription = "", colorFilter = ColorFilter.tint(Color.White))
            Text(text = "Ładowanie!", color = Color.White)
        }
    }
}