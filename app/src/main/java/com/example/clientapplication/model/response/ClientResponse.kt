package com.example.clientapplication.model.response

import com.example.clientapplication.model.Commande

data class ClientResponse(
    val email : String?,
    val nom: String?,
    val prenom: String?,
    val adresse: String?,
    val telephone: String?
)