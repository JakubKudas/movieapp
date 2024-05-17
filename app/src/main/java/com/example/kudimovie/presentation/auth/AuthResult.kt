package com.example.kudimovie.presentation.auth

sealed class AuthResult {
    data class Success(val message: String): AuthResult()
    data class Failure(val errorMessage: String, val errorCode: String? = null): AuthResult()
}
