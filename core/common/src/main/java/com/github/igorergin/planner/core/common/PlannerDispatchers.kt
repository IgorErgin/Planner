package com.github.igorergin.planner.core.common

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME


enum class PlannerDispatchers {
    Default,
    IO,
}

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val plannerDispatcher: PlannerDispatchers)