package com.example.kudimovie.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kudimovie.R
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.ui.theme.navbarColor
import com.example.kudimovie.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(id: Int, viewModel: MainViewModel) {


    var isMovieInFavourites by remember { mutableStateOf(viewModel.favourites.any {it == id})}
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {SnackbarHostState()}

    val movie = viewModel.movie
    LaunchedEffect(viewModel) {
        viewModel.fetchMovie(id)
    }

    if (movie == null) {
        CircularProgressIndicator()
    } else {
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
        ) {padding ->
            PaddingValues(0.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${movie?.title}",
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .fillMaxHeight()
                                .fillMaxWidth()
                        )
                    }

                }
                item {
                    val posterUrl = "https://image.tmdb.org/t/p/w185${movie?.posterPath}"
                    val painter = rememberAsyncImagePainter(model = posterUrl)

                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(350.dp)
                                .fillMaxWidth(0.65f)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(
                                        topStartPercent = 10,
                                        bottomStartPercent = 10
                                    )
                                )
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null, // provide a meaningful description
                                alignment = Alignment.Center,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Column(
                            modifier = Modifier
                                .height(350.dp)
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(
                                        bottomEndPercent = 15,
                                        topEndPercent = 15
                                    )
                                )
                                .background(
                                    navbarColor,
                                    shape = RoundedCornerShape(
                                        topEndPercent = 10,
                                        bottomEndPercent = 10
                                    )
                                )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.87f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Gatunki:", modifier = Modifier.padding(10.dp))
                                movie?.genres?.forEach {
                                    Text(text = "${it.name}", modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                            if(viewModel.isUserLoggedIn() && !viewModel.isUserAnonymous()){
                                Image(
                                    imageVector = if(isMovieInFavourites) Icons.Rounded.Clear else Icons.Rounded.Add,
                                    contentDescription = "",
                                    contentScale = ContentScale.FillBounds,
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .offset(x = 80.dp)
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(100))
                                        .background(Color.White.copy(0.15f))
                                        .clickable {
                                            if (viewModel.favourites.any { it == id }) {
                                                viewModel.removeFavouriteMovie(id)
                                            } else {
                                                viewModel.addFavoriteMovie(id)
                                            }
                                            isMovieInFavourites = !isMovieInFavourites
                                            val snackbarMessage =
                                                if (isMovieInFavourites) "Dodano do ulubionych" else "Usunięto z ulubionych"
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    snackbarMessage,
                                                    duration = SnackbarDuration.Short,
                                                    actionLabel = "Ok"
                                                )
                                            }
                                        }
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, end = 5.dp)
                                .fillMaxWidth(0.5f)
                                .shadow(2.dp, RoundedCornerShape(60))
                                .height(37.dp)
                                .clip(RoundedCornerShape(60))
                                .background(navbarColor)
                        ) {
                            Text(
                                text = "${String.format("%.2f", movie?.voteAverage)}",
                                color = Color.White
                            )
                            Image(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(Color.Yellow)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 5.dp)
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(60))
                                .height(37.dp)
                                .clip(RoundedCornerShape(60))
                                .background(navbarColor)
                        ) {
                            Text(
                                text = if ((movie?.runtime) != null) "${movie.runtime / 60}h ${movie.runtime % 60}min" else "",
                                color = Color.White,
                            )
                            Image(
                                painter = painterResource(R.drawable.schedule),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }

                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (movie?.overview != "") {
                            Text(
                                text = "${movie?.overview}",
                                color = Color.White,
                                modifier = Modifier.fillMaxSize(),
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }
            }
        }
    }
}