package com.github.planner.core.navigation

interface AppNavigator {
    fun navigateTo(route: String)
    fun back()
}