package io.alron.planner.presentation.calendar_and_tasks

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.alron.planner.R
import io.alron.planner.presentation.util.toRuFormat
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarAndTasksScreen(modifier: Modifier = Modifier) {
    val calendarState = rememberSelectableCalendarState()
    val selectedDate = calendarState.selectionState.selection.firstOrNull()

    val viewModel: CalendarAndTasksViewModel = hiltViewModel()
    val slotsState = viewModel.slotsState.collectAsStateWithLifecycle().value

    var showSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            viewModel.loadTasks(it)
        }
    }

    LaunchedEffect(selectedDate) {
        viewModel.events.collect { event ->
            when (event) {
                is CalendarAndTasksEvent.TaskAdded -> {
                    showSheet = false
                    if (event.date == selectedDate) {
                        viewModel.loadTasks(selectedDate)
                    }
                    Toast.makeText(
                        context, context.getString(R.string.task_is_added),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                CalendarAndTasksEvent.TaskDeleted -> {
                    Toast.makeText(
                        context, context.getString(R.string.task_is_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CalendarAndTasksEvent.Error -> {
                    Toast.makeText(
                        context, event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            AddTaskActionButton(
                onClick = { showSheet = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            CalendarAndTasksTopBar()
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                CustomCalendar(calendarState)
                Spacer(Modifier.height(28.dp))
                when (selectedDate) {
                    null -> Text(
                        text = stringResource(R.string.choose_day),
                        style = MaterialTheme.typography.titleMedium
                    )

                    else -> {
                        Text(
                            text = stringResource(
                                R.string.task_list_for_date,
                                selectedDate.toRuFormat()
                            ),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        when (slotsState) {
                            is SlotState.Content -> {
                                slotsState.slots.forEach { slot ->
                                    TimeAndTaskItem(
                                        hour = slot.hour,
                                        task = slot.task,
                                        duration = slot.duration,
                                        onDelete = {
                                            slot.task?.let {
                                                viewModel.deleteTask(it.id, selectedDate)
                                            }
                                        }
                                    )
                                }
                            }

                            is SlotState.Error -> {
                                Text(slotsState.message)
                            }

                            SlotState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                    ) {
                        AddTaskSheetContent(
                            onAdd = { name, desc, taskDate, start, end ->
                                viewModel.addTask(
                                    name,
                                    desc,
                                    taskDate,
                                    start,
                                    end
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}