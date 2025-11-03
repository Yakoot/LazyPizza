package dev.mamkin.lazypizza.auth.domain

sealed class AuthException(message: String) : Exception(message) {
    class InvalidPhoneNumber : AuthException("Invalid phone number format")
    class InvalidVerificationCode : AuthException("Invalid verification code")
    class NetworkError : AuthException("Network error occurred")
    class TooManyRequests : AuthException("Too many requests. Please try again later")
    class UnknownError(message: String = "Unknown error occurred") : AuthException(message)
}