package com.example.clientapplication.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commandes")

data class CommandesResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val commandes: List<CommandeResponse>?
)