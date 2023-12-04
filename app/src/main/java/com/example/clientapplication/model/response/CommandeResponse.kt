package com.example.clientapplication.model.response

import com.example.clientapplication.model.StatusCommande
import java.util.Date

data class CommandeResponse(

    val id: Long?,
    val status: StatusCommande?,
    val client_id: String?,
    val produits: List<ProduitQuantiteResponse>?,
    val date: String?
)