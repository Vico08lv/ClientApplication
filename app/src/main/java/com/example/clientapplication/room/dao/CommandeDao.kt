package com.example.clientapplication.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.clientapplication.model.Commande
import com.example.clientapplication.model.response.CommandesResponse

@Dao
interface CommandeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommande(commandes: CommandesResponse)

    @Query("SELECT * FROM commandes")
    fun getAllCommandes(): CommandesResponse

}
