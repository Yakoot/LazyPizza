package dev.mamkin.lazypizza.auth.presentation.signin

data class SignInState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
