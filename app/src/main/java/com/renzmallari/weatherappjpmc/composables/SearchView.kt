package com.renzmallari.weatherappjpmc.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.renzmallari.weatherappjpmc.weather.WeatherViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    viewModel: WeatherViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .onKeyEvent { false },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if(state.value != TextFieldValue("")){
                IconButton(
                    onClick = {
                        state.value = TextFieldValue("")
                    }
                ){
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                viewModel.getWeatherByCityName(state.value.text)
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
}