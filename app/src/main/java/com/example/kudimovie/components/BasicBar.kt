package com.example.kudimovie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.example.kudimovie.ui.theme.borderColor

@Composable
fun BasicBar(
    modifier: Modifier,
    onDone: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    decorationBox: @Composable (@Composable () -> Unit) -> Unit = { innerTextField -> innerTextField() },
    isFocused: Boolean = false,
    fontSize: Int = 0,
    passText: (String) -> Unit
){
    var textFieldValue by remember { mutableStateOf("") }
    var isFocus = isFocused
    passText(textFieldValue)

    BasicTextField(
        value = textFieldValue,
        onValueChange = {newText ->
            textFieldValue = newText
        },
        singleLine = true,
        modifier = modifier,
        textStyle = TextStyle(
            color = if(isFocus) Color.White else borderColor,
            fontSize = if(fontSize == 0) 20.sp else fontSize.sp,
            textAlign = TextAlign.Left,
            textIndent = TextIndent(firstLine = 12.sp)
        ),
        cursorBrush = Brush.linearGradient(listOf(Color.White, Color.White)),
        keyboardActions = KeyboardActions(
            onDone = { onDone(textFieldValue) }
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = decorationBox
    )
}