package com.example.clientapplication.model.response

import com.example.clientapplication.model.StatusProduitQuantite
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "produits_quantite")
data class ProduitQuantiteResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quantite: Int?,
    val statusProduitQuantite: StatusProduitQuantite?
)
