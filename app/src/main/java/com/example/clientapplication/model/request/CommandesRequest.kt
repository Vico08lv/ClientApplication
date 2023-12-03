package com.example.clientapplication.model.request

import com.example.clientapplication.model.response.CommandeResponse

data class CommandesRequest(
    val commandes: ArrayList<CommandeResponse>
)