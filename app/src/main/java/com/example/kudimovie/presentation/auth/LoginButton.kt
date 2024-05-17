package com.example.kudimovie.presentation.auth

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import com.example.kudimovie.components.BasicButton
import com.example.kudimovie.screens.SnackbarForErrors
import com.example.kudimovie.screens.checkPswd
import com.example.kudimovie.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginButton(
    email: String,
    password: String,
    viewModel: MainViewModel,
    focusManager: FocusManager,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    var authResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authResult, viewModel.authResult) {
        if (viewModel.authResult is AuthResult.Failure) {
            authResult = (viewModel.authResult as AuthResult.Failure).errorMessage
        }
    }

    if (authResult != null) {
        SnackbarForErrors(authResult, coroutineScope, snackbarHostState, viewModel)
    }

    BasicButton(onClick = {
        if(email.length > 1 && checkPswd(password)){
            viewModel.authenticateWithEmailAndPassword(email, password)
            focusManager.clearFocus()

        }else if(email.isEmpty()){
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    "Proszę wprowadzić adres email!",
                    duration = SnackbarDuration.Short,
                    actionLabel = "Ok"
                )
            }
        } else if(!checkPswd(password)){
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    "Hasło jest zbyt słabe!",
                    duration = SnackbarDuration.Short,
                    actionLabel = "Ok"
                )
            }
        } },
        textContent = "Zaloguj się"
    )

}