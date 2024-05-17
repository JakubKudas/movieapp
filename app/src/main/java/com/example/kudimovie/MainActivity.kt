package com.example.kudimovie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.kudimovie.screens.MainScreen
import com.example.kudimovie.ui.theme.KudiMovieTheme
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.viewmodels.MainViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        if (viewModel.isUserLoggedIn() && !viewModel.isUserAnonymous()) {
            viewModel.loadFavoriteMoviesFromFirestore()
        }
        installSplashScreen().setKeepOnScreenCondition() {
            false
        }

        setContent {
            KudiMovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    MainScreen(navController = rememberNavController(), viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (viewModel.isUserLoggedIn() && !viewModel.isUserAnonymous()){
            viewModel.saveFavoriteMoviesToFirestore()
        }
    }
    override fun onPause() {
        super.onPause()

        if (viewModel.isUserLoggedIn() && !viewModel.isUserAnonymous()){
            viewModel.saveFavoriteMoviesToFirestore()
        }
    }
    override fun onStop() {
        super.onStop()

        if (viewModel.isUserLoggedIn() && !viewModel.isUserAnonymous()){
            viewModel.saveFavoriteMoviesToFirestore()
        }
    }
}
