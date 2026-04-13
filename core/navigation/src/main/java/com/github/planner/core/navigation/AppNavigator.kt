package com.github.planner.core.navigation

interface AppNavigator {
    fun navigateTo(route: Any)
    fun back()
}