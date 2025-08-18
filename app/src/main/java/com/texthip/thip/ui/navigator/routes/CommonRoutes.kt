package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class CommonRoutes : Routes() {
    @Serializable
    data object Alarm : CommonRoutes()

    @Serializable
    data object Splash : CommonRoutes()

    @Serializable
    data object Login : CommonRoutes()

    @Serializable
    data object Signup : CommonRoutes()
    
    @Serializable
    data object RegisterBook : CommonRoutes()
}
