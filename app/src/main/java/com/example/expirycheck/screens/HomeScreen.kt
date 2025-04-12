package com.example.expirycheck.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.FAB
import com.example.expirycheck.customs.FoodExpiryCard
import com.example.expirycheck.customs.NoItemsText
import com.example.expirycheck.customs.SectionHeader
import com.example.expirycheck.customs.SummaryCard
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.viewmodel.ItemsViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    ivm: ItemsViewModel,
    sharedPreferencesManager: PreferencesRepository,
) {
    val activity = LocalActivity.current as ComponentActivity
    var username by remember {
        mutableStateOf("")
    }
    val items by remember { derivedStateOf { ivm.items } }
    val isLoading by remember { derivedStateOf { ivm.isLoading } }

    LaunchedEffect(Unit) {
        username = sharedPreferencesManager.getUsername().toString()
        if (username.isNotEmpty()) {
            ivm.getItems(username)
        }
    }


    val today = LocalDate.now()
    val weekAhead = today.plusDays(7)

    val expiredItemsList by remember(items) {
        derivedStateOf {
            items.filter { !isDateValid(it.expiryDate, today) }
                .sortedBy { parseDate(it.expiryDate) }
        }
    }

    val currentItems by remember(items) {
        derivedStateOf {
            items.filter { isDateValid(it.expiryDate, today) }
        }
    }

    val expiringSoonItems by remember(items, today, weekAhead) {
        derivedStateOf {
            currentItems.filter { isExpiringSoon(it.expiryDate, today, weekAhead) }
                .sortedBy { parseDate(it.expiryDate) }
        }
    }

    BackHandler { activity.finish() }

    Scaffold(
        topBar = { TopAppBar("${getGreetingMessage()}, $username 👋") },
        floatingActionButton = { FAB { navController.navigate(Routes.AddItems.routes) } },
        bottomBar = { BottomAppBar(navController = navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SummaryCard(
                                    "📦 Tracked Items",
                                    items.size,
                                    MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCard(
                                    "🛒 Current Items",
                                    currentItems.size,
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SummaryCard(
                                    "⚠️ Expired Items",
                                    expiredItemsList.size,
                                    MaterialTheme.colorScheme.errorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCard(
                                    "⏳ Expiring Soon",
                                    expiringSoonItems.size,
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    if (expiringSoonItems.isNotEmpty()) {
                        item { SectionHeader("⏳ Expiring Within a Week") }
                        items(expiringSoonItems) { item ->
                            FoodExpiryCard(
                                item = item.itemName,
                                date = item.expiryDate,
                                ivm = ivm,
                                id = item.id
                            )
                        }
                    } else {
                        item { NoItemsText("No items expiring within a week!") }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    // Expired Items Section
                    if (expiredItemsList.isNotEmpty()) {
                        item { SectionHeader("⚠️ Expired Items", MaterialTheme.colorScheme.error) }
                        items(expiredItemsList) { item ->
                            FoodExpiryCard(
                                item = item.itemName,
                                date = item.expiryDate,
                                id = item.id,
                                ivm = ivm
                            )
                        }
                    } else {
                        item { NoItemsText("No expired items!") }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

fun getGreetingMessage(): String {
    val now = LocalTime.now()
    return when (now.hour) {
        in 5..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

fun isDateValid(expiryDate: String, today: LocalDate): Boolean {
    return parseDate(expiryDate)?.isAfter(today) ?: false
}

fun isExpiringSoon(expiryDate: String, today: LocalDate, weekAhead: LocalDate): Boolean {
    return parseDate(expiryDate)?.isAfter(today)
        ?.and(parseDate(expiryDate)?.isBefore(weekAhead) == true) ?: false
}

fun parseDate(dateString: String): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
        LocalDate.parse(dateString, formatter)
    } catch (e: Exception) {
        null
    }
}
