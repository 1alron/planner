package io.alron.planner.presentation.calendar_and_tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.alron.planner.R
import io.alron.planner.presentation.util.WORKING_FINISH_HOUR
import io.alron.planner.presentation.util.WORKING_START_HOUR
import io.alron.planner.presentation.util.toRuFormat
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSheetContent(
    onAdd: (
        name: String,
        description: String,
        date: LocalDate,
        startHour: Int,
        endHour: Int
    ) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var startHour by rememberSaveable { mutableIntStateOf(WORKING_START_HOUR) }
    var endHour by rememberSaveable { mutableIntStateOf(WORKING_START_HOUR + 1) }

    val context = LocalContext.current

    val hours = (WORKING_START_HOUR..WORKING_FINISH_HOUR).toList()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.new_task),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.date_with_formatting, date.toRuFormat()))
            Spacer(Modifier.width(12.dp))
            AddTaskSheetButton(
                onClick = {
                    android.app.DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            date = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        date.year,
                        date.monthValue - 1,
                        date.dayOfMonth
                    ).show()
                },
                text = stringResource(R.string.choose_date)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HourDropdown(
                label = stringResource(R.string.start),
                selectedHour = startHour,
                hours = hours,
                maxHour = WORKING_FINISH_HOUR - 1,
                onHourSelected = { h ->
                    startHour = h
                    if (endHour <= startHour) endHour =
                        (startHour + 1).coerceAtMost(WORKING_FINISH_HOUR)
                },
            )

            Spacer(Modifier.width(8.dp))

            HourDropdown(
                label = stringResource(R.string.finish),
                selectedHour = endHour,
                hours = hours,
                minHour = startHour + 1,
                onHourSelected = { h -> endHour = h },
            )
        }
        Spacer(Modifier.height(16.dp))
        AddTaskSheetButton(
            onClick = {
                onAdd(
                    name,
                    description,
                    date,
                    startHour,
                    endHour
                )
            },
            text = stringResource(R.string.add_task),
            enabled = name.isNotBlank() && description.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}