package com.example.clientapplication.model.request

import com.example.clientapplication.model.StatusProduitQuantite

data class ProduitQuantiteRequest (
    val id: Long?,
    var quantite: Int?,
//    val statusProduitQuantite: StatusProduitQuantite?
    )