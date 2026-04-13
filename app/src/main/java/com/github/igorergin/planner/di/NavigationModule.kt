package com.github.igorergin.planner.di

import com.github.igorergin.planner.AppNavigatorImpl
import com.github.planner.core.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}