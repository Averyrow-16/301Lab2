package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it)}, //it is the city name
                        onDeleteCity = { cityRepository.deleteCity(it)}, //it is the city name
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

class CityRepository {
    //private so it cant be changed by other classes directly
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka", "New Delhi"
    )

    val cities: List<String> //gives ui read only access so it can display cities
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}
//@composable means the function descris part of apps ui
@Composable
fun CityListScreen(
    cities: List<String>, //list of city names received from MainActivity
    onAddCity: (String) -> Unit, //unit means function returns nothing but it performs an action
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier //allows layout info such as padding to be passed into screen
)
{
    //initializes with empty string
    //when value in mutableStateOf changes, Compose will automatically trigger a recomposition (re-render ui components reading the variable)
    //remember tells compose to store state object in memory across recompositions
    //by  allows us to treat newCityName as a standard string instead of getting newCityName.value
    var newCityName by remember {mutableStateOf("")}
    var selectCityName by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField (
                value = newCityName,
                onValueChange = { newCityName = it},
                label = {Text("City name")},
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (selectCityName.isNotBlank()) {
                        onDeleteCity(selectCityName)
                        selectCityName = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }

        //LazyColumn is Compose for basic scrolling ListView
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // items(cities) loops through the city list and creates 1 UI row for each city
            items(cities) {
                city: String -> CityRow(
                city = city,
                onCityClick = { clickedCity: String -> selectCityName = clickedCity},
                isSelected = (city == selectCityName)
                )
            }
        }
    }

}

@Composable
fun CityRow(city: String, isSelected: Boolean = false, onCityClick: (String) -> Unit = {}) {
    Text(
        text = city,
        fontSize = 28.sp,
        color = if (isSelected) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Unspecified,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCityClick(city)}
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}