package com.renzmallari.weatherappjpmc.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.renzmallari.weatherappjpmc.util.Constants
import com.renzmallari.weatherappjpmc.weather.WeatherViewModel
import java.util.Locale


fun List<String>.filterByText(text: String): MutableList<String> {
    val list = mutableListOf<String>()
    for (item in this) {
        if (item.lowercase(Locale.getDefault())
                .contains(text.lowercase(Locale.getDefault()))
        ) {
            list.add(item)
        }
    }
    return list
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemList(
    state: MutableState<TextFieldValue>,
    viewModel: WeatherViewModel,
    navController: NavController
) {
    val items = Constants.US_CITY_LIST
    val keyboardController = LocalSoftwareKeyboardController.current

    var filteredItems: List<String>
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
    ) {
        val searchText = state.value.text
        filteredItems = items.filterByText(searchText)

       items(filteredItems){ filteredItem ->
           ListItem(
               itemText = filteredItem,
               onItemClicked = {
                   viewModel.getWeatherByCityName(it)
                   keyboardController?.hide()
                   navController.navigate("info")
               }
           )
       }
    }
}

@Composable
fun ListItem(
    itemText:String,
    onItemClicked: (String) -> Unit
) {
   Row(
      modifier = Modifier
          .clickable { onItemClicked(itemText) }
          .background(Color.LightGray)
          .height(60.dp)
          .fillMaxWidth()
          .padding(8.dp)
   ) {
       Text(
           text = itemText,
           fontSize = 18.sp
       )
   }
}
