package com.github.igorergin.planner

import android.app.Application
import com.github.igorergin.planner.core.common.di.ApplicationScope
import com.github.igorergin.planner.data.repository.TaskRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class PlannerApplication : Application() {

    @Inject
    lateinit var repository: TaskRepository

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            repository.prePopulateDataIfNeeded()
        }
    }
}