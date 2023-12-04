package com.example.clientapplication.model.response

import com.example.clientapplication.model.CategorieProduit

data class ProduitResponse(
    val id : Long?,
    val nom :String?,
    val prix : Double?,
    val description : String?,
    val quantite : Int?,
    val emailProducteur : String?,
    val date_publication : String?,
    val categorie : CategorieProduit?
)