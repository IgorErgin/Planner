package com.github.igorergin.planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.igorergin.planner.feature.task_editor.api.TaskEditorRoute
import com.github.igorergin.planner.feature.task_editor.impl.presentation.TaskEditorScreen
import com.github.igorergin.planner.feature.task_editor.impl.presentation.TaskEditorViewModel
import com.github.igorergin.planner.feature.task_list.api.TaskListRoute
import com.github.igorergin.planner.feature.task_list.impl.presentation.TaskListScreen
import com.github.igorergin.planner.feature.task_list.impl.presentation.TaskListViewModel
import com.github.planner.core.ui.theme.PlannerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: AppNavigatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                navigator.navigationEvents.collectLatest { command ->
                    when (command) {
                        is NavigationCommand.To -> navController.navigate(command.route)
                        is NavigationCommand.Back -> navController.popBackStack()
                    }
                }
            }

            PlannerTheme {
                NavHost(
                    navController = navController,
                    startDestination = TaskListRoute
                ) {
                    composable<TaskListRoute> {
                        TaskListScreen(
                            viewModel = hiltViewModel<TaskListViewModel>(),
                            onNavigateToEditor = { taskId ->
                                navController.navigate(TaskEditorRoute(taskId = taskId))
                            }
                        )
                    }

                    composable<TaskEditorRoute> {
                        TaskEditorScreen(
                            viewModel = hiltViewModel<TaskEditorViewModel>(),
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}