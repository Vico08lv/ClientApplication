package com.example.clientapplication.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clientapplication.model.StatusCommande

@Entity(tableName = "commande")
data class CommandeResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val status: StatusCommande?,
    val clientId: String?,
    val produits: List<ProduitQuantiteResponse>?,
    val date: String?
)
