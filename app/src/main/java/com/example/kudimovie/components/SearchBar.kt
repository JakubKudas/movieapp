package com.example.kudimovie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kudimovie.ui.theme.backgroundColor
import com.example.kudimovie.ui.theme.bone
import com.example.kudimovie.ui.theme.borderColor
import com.example.kudimovie.ui.theme.searchBarColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(focusManager: FocusManager,
              onDone: (String) -> Unit,
              passText: (String) -> Unit
) {
    var isFocused by remember{ mutableStateOf(true)}

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(top = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicBar(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .defaultMinSize(minHeight = 20.dp)
                .clip(RoundedCornerShape(50))
                .background(searchBarColor)
                .border(
                    width = 1.dp,
                    brush = if(isFocused) Brush.linearGradient(
                        listOf(Color.White, Color.White))
                    else Brush.linearGradient(listOf(borderColor, borderColor)),
                    shape = RoundedCornerShape(50)
                )
                .onFocusChanged { isFocused = !isFocused },
            onDone = { onDone(it) },
            fontSize = 16,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
                ),
            decorationBox = {textField ->
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Image(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "",
                        colorFilter = if(isFocused) ColorFilter.tint(Color.White) else ColorFilter.tint(
                            borderColor),
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .padding(start = 12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.85f),
                        contentAlignment = Alignment.CenterStart
                    ){
                        textField()
                    }
                }
            },
            isFocused = isFocused,
            passText = {}
        )
    }
}
