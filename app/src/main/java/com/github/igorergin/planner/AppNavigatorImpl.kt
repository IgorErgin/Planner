package com.github.igorergin.planner

import com.github.planner.core.navigation.AppNavigator
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private val _navigationEvents = MutableSharedFlow<NavigationCommand>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    override fun navigateTo(route: Any) {
        _navigationEvents.tryEmit(NavigationCommand.To(route))
    }

    override fun back() {
        _navigationEvents.tryEmit(NavigationCommand.Back)
    }
}


sealed interface NavigationCommand {
    data class To(val route: Any) : NavigationCommand
    data object Back : NavigationCommand
}