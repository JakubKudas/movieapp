package com.example.kudimovie.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.ui.theme.borderColor
import com.example.kudimovie.ui.theme.searchBarColor

@Composable
fun BasicButton(onClick: () -> Unit, textContent: String){
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(0.4f)
            .clip(RoundedCornerShape(40)),
        colors = ButtonDefaults.buttonColors(
            containerColor = searchBarColor,
            contentColor = Color.White,
            disabledContainerColor = bone,
            disabledContentColor = bone
        ),
        border = BorderStroke(1.dp, Brush.linearGradient(listOf(borderColor, borderColor)))
    ) {
        Text(text = textContent, textAlign = TextAlign.Center, modifier = Modifier.background(
            Color.Transparent))
    }
}