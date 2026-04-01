package io.alron.planner.presentation.calendar_and_tasks

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourDropdown(
    label: String,
    selectedHour: Int,
    hours: List<Int>,
    modifier: Modifier = Modifier,
    minHour: Int? = null,
    maxHour: Int? = null,
    onHourSelected: (Int) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val filteredHours = hours.filter { h ->
        (minHour == null || h >= minHour) && (maxHour == null || h <= maxHour)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = "$selectedHour:00",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredHours.forEach { h ->
                DropdownMenuItem(
                    text = { Text("$h:00") },
                    onClick = {
                        onHourSelected(h)
                        expanded = false
                    }
                )
            }
        }
    }
}