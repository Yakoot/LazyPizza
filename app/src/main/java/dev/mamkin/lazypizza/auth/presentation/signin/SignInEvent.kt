package dev.mamkin.lazypizza.auth.presentation.signin

sealed interface SignInEvent {
    data object BackToHome : SignInEvent
}
