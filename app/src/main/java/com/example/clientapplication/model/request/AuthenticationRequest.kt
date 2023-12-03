package com.example.clientapplication.model.request

data class AuthenticationRequest(
    val email: String,
    val password: String,
    val tokenApp: String
)