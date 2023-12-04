package com.example.clientapplication.model.request

import com.example.clientapplication.model.StatusCommande
import java.util.Date

data class CommandeRequest(
//    val id: Long?,
//    val status: StatusCommande?,
//    var cliend_id: String?,
    var produits: List<ProduitQuantiteRequest>,
//    val date: Date
)