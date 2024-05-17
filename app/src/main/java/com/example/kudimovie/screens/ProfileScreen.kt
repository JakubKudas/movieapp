package com.example.kudimovie.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.kudimovie.presentation.auth.LoginBar
import com.example.kudimovie.presentation.auth.PasswordBar
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.R
import com.example.kudimovie.presentation.auth.AnonymousButton
import com.example.kudimovie.presentation.auth.AuthResult
import com.example.kudimovie.presentation.auth.ForgotPasswordDialog
import com.example.kudimovie.presentation.auth.LoginButton
import com.example.kudimovie.presentation.auth.LogoutButton
import com.example.kudimovie.presentation.auth.RegisterButton
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.ui.theme.navbarColor
import com.example.kudimovie.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    var loginType by remember { mutableStateOf("none") }
    var forceRecomposition by remember { mutableStateOf(false) }
    var loggedWith by remember { mutableStateOf("") }

    var isForgotPasswordDialogVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val authSuccess by viewModel.authSuccess.collectAsState()


    LaunchedEffect(authSuccess) {
        val message: String
        if (viewModel.isUserLoggedIn() && loginType == "login") message = "Zalogowano pomyślnie!"
        else if (viewModel.isUserLoggedIn() && loginType == "register") message = "Zarejestrowano pomyślnie!"
        else if(!viewModel.isUserLoggedIn()) message = "Wylogowano pomyślnie!"
        else message = "Zalogowano anonimowo!"
        if(authSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                    actionLabel = "Ok"
                )
            }
        }
        viewModel.resetAuthSuccessState()
    }
    Log.d("isLogged", viewModel.isUserLoggedIn().toString() + " " + loginType)
    Log.d("isLoggedSucces", authSuccess.toString())

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                Snackbar(
                    snackbarData = it,
                    containerColor = navbarColor,
                    contentColor = bone.copy(0.8f),
                    actionColor = bone,
                    shape = RoundedCornerShape(15)
                )
            }
        }
    ) { paddingValues -> PaddingValues(0.dp)
        paddingValues

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { focusManager.clearFocus() },
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.kudimovie_logo),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                )
                Text(loggedWith)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.isUserLoggedIn() && viewModel.getLoginType() != "anonymous") {
                    LogoutButton(viewModel = viewModel)
                    LaunchedEffect(loginType) {
                        loggedWith = "Zalogowano emailem"
                    }
                } else if (viewModel.isUserLoggedIn() && viewModel.getLoginType() == "anonymous") {
                    LaunchedEffect(key1 = loginType){
                        loggedWith = "Zalogowano anonimowo"
                    }
                    Row(
                        modifier = Modifier.height(100.dp)
                    ) {
                        TextButton(onClick = { loginType = "login" }) {
                            Text(text = "Logowanie")
                        }
                        TextButton(onClick = { loginType = "register" }) {
                            Text(text = "Rejestracja")
                        }
                    }
                    if(loginType == "login") {
                        Login(
                            focusManager = focusManager,
                            viewModel = viewModel,
                            coroutineScope,
                            snackbarHostState
                        ) {
                            isForgotPasswordDialogVisible = it
                        }
                        LaunchedEffect(loginType) {
                            loggedWith = "Logowanie"
                        }
                    }
                    if(loginType == "register") {
                        Register(
                            focusManager = focusManager,
                            viewModel = viewModel,
                            coroutineScope = coroutineScope,
                            snackbarHostState = snackbarHostState
                        )
                        LaunchedEffect(loginType) {
                            loggedWith = "Rejestracja"
                        }
                    }

                } else if (!viewModel.isUserLoggedIn()){
                    LaunchedEffect(loginType) {
                        loggedWith = "Nie zalogowano"
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            TextButton(
                                onClick = { loginType = "login" }
                            ) {
                                Text(text = "Logowanie")
                            }
                            TextButton(onClick = { loginType = "register" }) {
                                Text(text = "Rejestracja")
                            }
                        }
                        TextButton(onClick = { loginType = "anonymous" }) {
                            Text(text = "Logowanie anonimowe")
                        }
                    }
                        if(loginType == "login") {
                            Login(
                                focusManager = focusManager,
                                viewModel = viewModel,
                                coroutineScope,
                                snackbarHostState
                            ) {
                                isForgotPasswordDialogVisible = it
                            }
                            LaunchedEffect(loginType) {
                                loggedWith = "Logowanie"
                            }
                        }
                        if(loginType == "register") {
                            Register(
                                focusManager = focusManager,
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                snackbarHostState = snackbarHostState
                            )
                            LaunchedEffect(loginType) {
                                loggedWith = "Rejestracja"
                            }
                        }
                        if(loginType == "anonymous") {
                            AnonymousButton(viewModel = viewModel)
                            LaunchedEffect(loginType) {
                                loggedWith = "Logowanie anonimowe"
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun SnackbarForErrors(
    authResult: String?,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel
) {
    LaunchedEffect(authResult) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                authResult.toString(),
                duration = SnackbarDuration.Short,
                actionLabel = "Ok"
            )
        }
        viewModel.resetResult()
    }
}

fun checkPswd(password: String): Boolean{
    val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=,./?]).{8,}\$")
    return regex.matches(password)
}

@Composable
fun Login(
    focusManager: FocusManager,
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    pswdDialogOpen: (Boolean) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isForgotPasswordDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isForgotPasswordDialogVisible) {
        if (isForgotPasswordDialogVisible) {
            pswdDialogOpen(true)
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LoginBar(focusManager = focusManager, passText = {email = it})
        }
        item {
            PasswordBar(focusManager = focusManager, passText = {password = it})
        }
        item {
            Row(Modifier.fillMaxWidth(0.67f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top) {
                TextButton(
                    onClick = { isForgotPasswordDialogVisible = !isForgotPasswordDialogVisible },
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    modifier = Modifier.height(22.dp)
                ) {
                    Text("Zmień hasło")
                }
            }}
        item {
            LoginButton(email, password, viewModel, focusManager, coroutineScope, snackbarHostState)
            }
    }

    if(isForgotPasswordDialogVisible) {
        ForgotPasswordDialog(
            onDismissRequest = { isForgotPasswordDialogVisible = false },
            onResetPassword = {text ->
                viewModel.resetPassword(text)
            },
            focusManager = focusManager
        )
    }
}
@Composable
fun Register(
    focusManager: FocusManager,
    viewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    LoginBar(focusManager = focusManager, passText = {email = it})
    PasswordBar(focusManager = focusManager, passText = {password = it})
    RegisterButton(email, password, viewModel, focusManager, coroutineScope, snackbarHostState)
}