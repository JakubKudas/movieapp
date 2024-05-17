package com.example.kudimovie.presentation.auth

import androidx.compose.runtime.Composable
import com.example.kudimovie.components.BasicButton
import com.example.kudimovie.viewmodels.MainViewModel

@Composable
fun AnonymousButton(viewModel: MainViewModel) {
    BasicButton(onClick = { viewModel.authenticateAnonymous() }, textContent = "Zaloguj sie anonimowo")
}
