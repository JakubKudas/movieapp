package com.example.kudimovie.presentation.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.data.model.Movie

@Composable
fun MovieItem(movie: Movie, key: Any, onClick: (Int) -> Unit) {
    val posterUrl = "https://image.tmdb.org/t/p/w185${movie.posterPath}"


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 10.dp)
            .layoutId(key)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.65F)
                .clip(RoundedCornerShape(10))
                .clickable {
                    onClick(movie.id)
                }
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize(0.98f)
                    .clip(RoundedCornerShape(10))
                    .shadow(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(10)
                    )
                    .background(Color.Black.copy(0.15f))
            ) {
                val painter = rememberAsyncImagePainter(model = posterUrl)

                Image(
                    painter = painter,
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize(0.99f)
                        .clip(RoundedCornerShape(10))
                        .background(brush = Brush.linearGradient(
                            listOf(Color.Transparent, Color.Black),
                            start = Offset(100f, 0f),
                            end = Offset(100f, 600f)
                            )),
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.9f
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(0.98f)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = movie.title,
                    style = TextStyle(fontSize = 14.sp),
                    color = bone,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
