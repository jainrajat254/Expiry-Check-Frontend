package com.example.expirycheck.customs

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.expirycheck.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarBox(
    value: String,
    label: String,
    placeholder: String,
    enabled: Boolean = true,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(
            value = value,
            onValueChange = {},
            label = label,
            placeholder = placeholder,
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = true
        )
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePickerDialog(context, onDateSelected) }
            )
        }
    }
}

fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val formattedDate =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}
