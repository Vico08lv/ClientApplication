package com.example.clientapplication.model.response

import com.example.clientapplication.model.StatusProduitQuantite

data class ProduitQuantiteResponse(
    val id : Long?,
    var quantite : Int?,
    val statusProduitQuantite: StatusProduitQuantite?
)