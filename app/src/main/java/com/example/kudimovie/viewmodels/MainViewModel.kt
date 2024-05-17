package com.example.kudimovie.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kudimovie.data.model.Movie
import com.example.kudimovie.data.remote.apiKey
import com.example.kudimovie.data.remote.movieApiService
import com.example.kudimovie.presentation.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _nowPlaying = mutableStateOf<List<Movie>?>(null)
    val nowPlaying: List<Movie>? get() = _nowPlaying.value

    private val _popularMovies = mutableStateOf<List<Movie>?>(null)
    val popularMovies: List<Movie>? get() = _popularMovies.value

    private val _searchedMovies = mutableStateOf<List<Movie>?>(null)
    val searchedMovies: List<Movie>? get() = _searchedMovies.value

    private val _movie = mutableStateOf<Movie?>(null)
    val movie: Movie? get() = _movie.value

    private val _authResult = mutableStateOf<AuthResult?>(null)
    val authResult: AuthResult? get()= _authResult.value

    private var _favouriteMovies = mutableStateOf<List<Movie>?>(null)
    val favouriteMovies: List<Movie>? get() = _favouriteMovies.value

    private var _favourites = mutableStateListOf<Int>()
    val favourites: List<Int> get() = _favourites

    private val _authSuccess = MutableStateFlow(false)
    val authSuccess: StateFlow<Boolean> = _authSuccess

    private val db = FirebaseFirestore.getInstance()
    private val favouritesCollection = db.collection("favourite")

    fun resetAuthSuccessState() {
        _authSuccess.value = false
    }

    fun resetResult() {
        _authResult.value = null
    }

    fun fetchNowPlaying() {
        viewModelScope.launch {
            val response = movieApiService.getPlayingMovies(apiKey)
            if (response.isSuccessful) {
                _nowPlaying.value = response.body()?.results.orEmpty()
            }
        }
    }

    fun fetchPopular() {
        viewModelScope.launch {
            val response = movieApiService.getPopularMovies(apiKey)
            if (response.isSuccessful) {
                _popularMovies.value = response.body()?.results.orEmpty()
            }
        }
    }

    fun fetchSearched(query: String) {
        viewModelScope.launch {
            val response = movieApiService.getSearchMovies(apiKey, query)
            if (response.isSuccessful) {
                _searchedMovies.value = response.body()?.results.orEmpty()
            }
        }
    }

    fun fetchMovie(id: Int) {
        viewModelScope.launch {
            val response = movieApiService.getMovie(id, apiKey)
            if (response.isSuccessful) {
                _movie.value = response.body()
            }
        }
    }

    fun authenticateAnonymous() {
        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    _authResult.value = null
                    _authSuccess.value = true
                } else {
                    val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                    _authResult.value = AuthResult.Failure("Login failed: ${task.exception?.message}", errorCode)
                    _authSuccess.value = false
                }
            }
    }

    fun authenticateWithEmailAndPassword(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = null
                    _authSuccess.value = true
                    loadFavoriteMoviesFromFirestore()
                } else {
                    val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                    _authResult.value = errorMessage(errorCode)?.let { AuthResult.Failure(it) }
                    _authSuccess.value = false
                }
            }
    }

    fun registerWithEmailPassword(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = null
                    _authSuccess.value = true
                } else {
                    val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                    _authResult.value = errorMessage(errorCode)?.let { AuthResult.Failure(it) }
                    _authSuccess.value = false
                }
            }
    }

    fun getLoginType(): String {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            if (currentUser.isAnonymous) {
                "anonymous"
            } else {
                "email"
            }
        } else {
            "none"
        }
    }

    fun logout() {
        saveFavoriteMoviesToFirestore()
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        _favourites = mutableStateListOf()
        _favouriteMovies = mutableStateOf<List<Movie>?>(null)
        _authSuccess.value = true
    }

    fun isUserLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }

    fun isUserAnonymous(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.isAnonymous == true
    }

    fun resetPassword(email: String) {
        val auth = FirebaseAuth.getInstance()

        if(email.isNotBlank()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        _authResult.value = AuthResult.Success("Wysłano email")
                    } else {
                        val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                        _authResult.value = AuthResult.Failure("Send failed: ${task.exception?.message}", errorCode)
                    }
                }
        }
    }

    fun addFavoriteMovie(movieId: Int) {
        if(isUserLoggedIn() && !isUserAnonymous()){
            if(!_favourites.any { it == movieId }){
                _favourites.add(movieId)
                _favourites = _favourites.distinct().toMutableStateList()
            }
            getFavoriteMovies()
        }
    }

    fun removeFavouriteMovie(movieId: Int) {
        if(isUserLoggedIn() && _favourites.any { it == movieId }){
            _favourites = _favourites.filterNot { it == movieId }.toMutableStateList()
            getFavoriteMovies()
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            val favoriteMoviesList = mutableListOf<Movie>()
            for (movieId in _favourites) {
                val response = movieApiService.getMovie(movieId, apiKey)
                if (response.isSuccessful) {
                    favoriteMoviesList.add(response.body()!!)
                }
            }
            _favouriteMovies.value = favoriteMoviesList.toList()
        }
    }

    fun saveFavoriteMoviesToFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let {
            val favoriteMoviesData = mapOf("favoriteMovies" to _favourites)
            favouritesCollection.document(uid)
                .set(favoriteMoviesData, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                    }
                }
        }
    }

    fun loadFavoriteMoviesFromFirestore() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let {
            favouritesCollection.document(uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            if (document.exists()) {
                                val favoriteMovies = document["favoriteMovies"] as? List<Int>
                                _favourites = (favoriteMovies ?: emptyList()).toMutableStateList()
                            } else {
                                _favourites = mutableStateListOf()
                            }
                        }
                    }
                }
        }
    }

    private fun errorMessage(errorCode: String?): String? {
        return when(errorCode) {
            "ERROR_INVALID_EMAIL" -> {
                "Podany adres email jest nieprawidłowy!"
            }
            "ERROR_INVALID_CREDENTIAL" -> {

                "Podane informacje są nieprawidłowe!"
            }
            "ERROR_WEAK_PASSWORD" -> {

                "Hasło jest zbyt słabe!"
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {

                "Adres email jest już w użyciu!"
            }
            else -> {
                "Podano błędne hasło!"
            }
        }
    }
}