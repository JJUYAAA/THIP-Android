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
    data object SignupFlow : CommonRoutes()

    @Serializable
    sealed interface SignupScreenRoutes {
        @Serializable
        data object Nickname : SignupScreenRoutes

        @Serializable
        data object Genre : SignupScreenRoutes

        @Serializable
        data object Tutorial : SignupScreenRoutes

        @Serializable
        data object Done : SignupScreenRoutes
    }

    @Serializable
    data object Main : CommonRoutes()
    
    @Serializable
    data object RegisterBook : CommonRoutes()
}
