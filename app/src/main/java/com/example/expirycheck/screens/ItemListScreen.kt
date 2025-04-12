package com.example.expirycheck.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.FAB
import com.example.expirycheck.customs.FoodExpiryCard
import com.example.expirycheck.customs.NoItemsToShow
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.viewmodel.ItemsViewModel

@Composable
fun ItemListScreen(
    navController: NavController,
    ivm: ItemsViewModel,
    sharedPreferencesManager: PreferencesRepository,
) {
    var username by remember { mutableStateOf("") }
    val items by remember { derivedStateOf { ivm.items } }

    LaunchedEffect(Unit) {
        username = sharedPreferencesManager.getUsername().toString()
        if (username.isNotEmpty()) {
            ivm.getItems(username)
        }
    }

    Scaffold(
        topBar = { TopAppBar("Food Expiration Dates") },
        floatingActionButton = { FAB { navController.navigate(Routes.AddItems.routes) } },
        bottomBar = { BottomAppBar(navController = navController) },
        content = { paddingValues ->
            if (items.isEmpty()) {
                NoItemsToShow(text = "No items found.\nTap + to add one!")
            } else {

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
                        items(items) { item ->
                            FoodExpiryCard(
                                item = item.itemName,
                                date = item.expiryDate,
                                id = item.id,
                                ivm = ivm
                            )
                        }
                    }
                }
            }
        }
    )
}
