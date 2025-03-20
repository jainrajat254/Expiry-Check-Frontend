package com.example.expirycheck.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.R
import com.example.expirycheck.customs.CalendarBox
import com.example.expirycheck.customs.CustomButton
import com.example.expirycheck.customs.CustomIconButton
import com.example.expirycheck.customs.CustomTextField
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen(navController: NavController, vm: UserViewModel) {

    var foodName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var openingDateRemember by remember { mutableStateOf(false) }
    var openingDate by remember { mutableStateOf("") }
    var timeSpan by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = "Add Item",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = "Food Name",
                placeholder = "Enter food name",
                trailingIcon = {
                    CustomIconButton(
                        iconRes = R.drawable.barcode,
                        contentDescription = "Barcode Scanner Icon",
                        onClick = {},
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            CalendarBox(
                value = expiryDate,
                label = "Expiry Date",
                placeholder = "Select expiry date",
                onDateSelected = { expiryDate = it }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = openingDateRemember,
                            onCheckedChange = { openingDateRemember = it })
                        Text(
                            text = "Opening Date (optional)",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    CalendarBox(
                        value = openingDate,
                        onDateSelected = { openingDate = it },
                        label = "Opening Date",
                        placeholder = "Enter opening date",
                        enabled = openingDateRemember
                    )
                    CustomTextField(
                        value = timeSpan,
                        onValueChange = { timeSpan = it },
                        label = "Time Span",
                        placeholder = "Enter number of days",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number,
                        enabled = openingDateRemember
                    )
                }
            }

            Row {
                CustomButton(
                    text = "Cancel",
                    onClick = { navController.navigate(Routes.Home.routes) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                )

                CustomButton(
                    text = "Add",
                    onClick = {
                        val message = when {
                            foodName.isBlank() -> "Please enter the item name"
                            expiryDate.isBlank() -> "Please select the expiry date"
                            else -> {
                                navController.navigate(Routes.Home.routes)
                                "Item added successfully"
                            }
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                )
            }
        }
    }
}
