package com.example.expirycheck.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.FAB
import com.example.expirycheck.customs.FoodExpiryCard
import com.example.expirycheck.customs.NoItemsText
import com.example.expirycheck.customs.SectionHeader
import com.example.expirycheck.customs.SummaryCard
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.models.FoodItem
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    val activity = LocalActivity.current as ComponentActivity

    val allTrackedItems = listOf(
        FoodItem("Milk", "March 21, 2025"),
        FoodItem("Bread", "March 22, 2025"),
        FoodItem("Biscuits", "March 25, 2025"),
        FoodItem("Cheese", "March 27, 2025"),
        FoodItem("Butter", "March 30, 2025"),
        FoodItem("Yogurt", "March 15, 2025")
    )

    val today = LocalDate.now()
    val weekAhead = today.plusDays(7)

    val expiredItemsList = allTrackedItems.filter { !isDateValid(it.expiryDate, today) }
        .sortedBy { parseDate(it.expiryDate) }
    val expiredItems = expiredItemsList.size

    val currentItems = allTrackedItems.filter { isDateValid(it.expiryDate, today) }
    val expiringSoonItems = currentItems.filter { isExpiringSoon(it.expiryDate, today, weekAhead) }
        .sortedBy { parseDate(it.expiryDate) }

    BackHandler { activity.finish() }

    Scaffold(
        topBar = { TopAppBar("${getGreetingMessage()}, Subhanshu 👋") },
        floatingActionButton = { FAB { navController.navigate(Routes.AddItems.routes) } },
        bottomBar = { BottomAppBar(navController = navController) }
    ) { paddingValues ->
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
                            allTrackedItems.size,
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
                            expiredItems,
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
                    FoodExpiryCard(item = item.name, date = item.expiryDate)
                }
            } else {
                item { NoItemsText("No items expiring within a week!") }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Expired Items Section
            if (expiredItemsList.isNotEmpty()) {
                item { SectionHeader("⚠️ Expired Items", MaterialTheme.colorScheme.error) }
                items(expiredItemsList) { item ->
                    FoodExpiryCard(item = item.name, date = item.expiryDate)
                }
            } else {
                item { NoItemsText("No expired items!") }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
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
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        LocalDate.parse(dateString, formatter)
    } catch (e: Exception) {
        null
    }
}
