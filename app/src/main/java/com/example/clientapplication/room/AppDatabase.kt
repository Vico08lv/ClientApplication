package com.example.clientapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clientapplication.model.Commande
import com.example.clientapplication.model.response.CommandesResponse
import com.example.clientapplication.room.dao.CommandeDao

@Database(entities = [CommandesResponse::class], version = 1, exportSchema = false)
@TypeConverters(CommandesResponseTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun commandeDao(): CommandeDao

    fun clearAllData() {
        INSTANCE?.clearAllTables()
    }
    companion object {
        // Singleton pour éviter la création multiple de la base de données
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Crée ou retourne l'instance unique de la base de données
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
