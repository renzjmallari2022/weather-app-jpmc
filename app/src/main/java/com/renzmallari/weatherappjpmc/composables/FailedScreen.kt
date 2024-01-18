package com.renzmallari.weatherappjpmc.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FailedScreen(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong! Please Try again",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Text(
            text = message,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

        Button(
            onClick = { /*onClickBackButton*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Text("Search Again")
        }
    }
}

@Preview
@Composable
fun PreviewFailedScreen() {
    FailedScreen(message = "Unable to fetch data.")
}