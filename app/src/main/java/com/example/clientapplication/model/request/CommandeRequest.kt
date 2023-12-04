package com.example.clientapplication.model.request

data class CommandeRequest(
//    val id: Long?,
//    val status: StatusCommande?,
//    var cliend_id: String?,
    var produits: MutableList<ProduitQuantiteRequest>,
//    val date: Date
)