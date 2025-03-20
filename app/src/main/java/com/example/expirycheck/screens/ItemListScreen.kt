package com.example.expirycheck.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.FoodExpiryCard
import com.example.expirycheck.models.FoodItem
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(navController: NavController, vm: UserViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Food Expiration Dates",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.AddItems.routes) }) {
                Icon(Icons.Filled.Add, "Add Icon")
            }
        },
        bottomBar = { BottomAppBar(navController = navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(foodList) { food ->
                        FoodExpiryCard(item = food.name, date = food.expiryDate)
                    }
                }
            }
        }
    )
}

val foodList = listOf(
    FoodItem("Apple", "Today"),
    FoodItem("Milk", "Tomorrow"),
    FoodItem("Eggs", "March 22"),
    FoodItem("Cheese", "March 25"),
    FoodItem("Apple", "Today"),
    FoodItem("Milk", "Tomorrow"),
    FoodItem("Eggs", "March 22"),
    FoodItem("Cheese", "March 25"),
    FoodItem("Apple", "Today"),
    FoodItem("Milk", "Tomorrow"),
    FoodItem("Eggs", "March 22"),
    FoodItem("Cheese", "March 25"),
)