package com.example.kudimovie.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kudimovie.components.BasicBar
import com.example.kudimovie.ui.theme.borderColor
import com.example.kudimovie.ui.theme.searchBarColor

@Composable
fun LoginBar(
    focusManager: FocusManager,
    passText: (String) -> Unit
){
    var isFocused by remember { mutableStateOf(true) }

    BasicBar(modifier = Modifier
        .clip(RoundedCornerShape(50))
        .fillMaxWidth(0.75f)
        .height(40.dp)
        .background(searchBarColor)
        .border(
            width = 1.dp,
            brush = if(isFocused) Brush.linearGradient(listOf(Color.White, Color.White)) else Brush.linearGradient(listOf(
                borderColor, borderColor)),
            shape = RoundedCornerShape(50)
        )
        .padding(top = 6.dp)
        .onFocusChanged { isFocused = !isFocused },
        onDone = {
            focusManager.clearFocus()
        },
        isFocused = isFocused,
        passText = {
            if(isFocused) passText(it)
        }
    )
}