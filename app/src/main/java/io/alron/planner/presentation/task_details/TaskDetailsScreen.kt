package io.alron.planner.presentation.task_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TaskDetailsScreen(
    taskId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<TaskDetailsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(taskId) {
        viewModel.getTask(taskId)
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            TaskDetailsToolbar(
                onBackClick = onBackClick,
                modifier = Modifier.padding(top = 12.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                when (state) {
                    is TaskDetailsState.Content -> {
                        TaskDetailsContent(
                            task = state.task
                        )
                    }
                    is TaskDetailsState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(state.message)
                        }
                    }
                    TaskDetailsState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}