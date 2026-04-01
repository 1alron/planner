package io.alron.planner.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.alron.planner.R
import io.alron.planner.presentation.calendar_and_tasks.CalendarAndTasksScreen
import io.alron.planner.presentation.task_details.TaskDetailsScreen

@Composable
fun PlannerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.CalendarAndTasks.route
    ) {
        composable(
            route = Route.CalendarAndTasks.route
        ) {
            CalendarAndTasksScreen(
                onTaskClick = { taskId ->
                    navController.navigate(Route.TaskDetails.createRoute(taskId))
                }
            )
        }

        composable(
            route = Route.TaskDetails.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            if (taskId == null) {
                Text(
                    text = stringResource(R.string.task_not_found),
                    modifier = Modifier
                        .safeDrawingPadding(),
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                TaskDetailsScreen(
                    taskId = taskId,
                    onBackClick = { navController.popBackStack() },
                    modifier = Modifier.safeDrawingPadding()
                )
            }

        }
    }
}