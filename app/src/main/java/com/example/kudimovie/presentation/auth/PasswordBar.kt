package com.example.kudimovie.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.kudimovie.R
import com.example.kudimovie.components.BasicBar
import com.example.kudimovie.ui.theme.borderColor
import com.example.kudimovie.ui.theme.searchBarColor

@Composable
fun PasswordBar(focusManager: FocusManager, passText: (String) -> Unit){
    var passwordVisible by remember{mutableStateOf(false)}
    var isFocused by remember{ mutableStateOf(true)}

    BasicBar(modifier = Modifier
        .clip(RoundedCornerShape(50))
        .fillMaxWidth(0.75f)
        .height(40.dp)
        .background(searchBarColor)
        .border(
            width = 1.dp,
            brush = if(isFocused) Brush.linearGradient(
                listOf(Color.White, Color.White))
                else Brush.linearGradient(listOf(borderColor, borderColor)),
            shape = RoundedCornerShape(50)
        )
        .onFocusChanged { isFocused = !isFocused },
        onDone = {
            focusManager.clearFocus()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        decorationBox = {textField ->
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.85f),
                    contentAlignment = Alignment.CenterStart
                ){
                    textField()
                }


                val icon = if(passwordVisible) R.drawable.visibility_on else R.drawable.visibility_off

                Image(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White.copy(0.85f)),
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            }
        },
        isFocused = isFocused,
        passText = {if(isFocused) passText(it)}
    )
}