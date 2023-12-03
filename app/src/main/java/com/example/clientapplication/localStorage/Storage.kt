package com.example.clientapplication.localStorage

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.clientapplication.model.Commande
import com.example.clientapplication.model.StatusCommande
import com.example.clientapplication.model.request.CommandeRequest
import com.example.clientapplication.model.request.ProduitQuantiteRequest
import com.example.clientapplication.model.response.ClientResponse
import com.example.clientapplication.model.response.ProduitQuantiteResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.util.Date
import java.util.Random
import java.util.UUID

class Storage(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("app_stock", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun saveToPreferences(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun retrieveFromPreferences(key: String, defaultValue: String): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun saveToken(value: String) {

        saveToPreferences("token", value)
    }

    fun getToken() : String {
        return retrieveFromPreferences("token", "")
    }

    fun saveProfil(clientResponse: ClientResponse) {
        clientResponse.nom?.let { saveToPreferences("nom", it) }
        clientResponse.prenom?.let { saveToPreferences("prenom", it) }
        clientResponse.adresse?.let { saveToPreferences("adresse", it) }
        clientResponse.email?.let { saveToPreferences("email", it) }
        clientResponse.telephone?.let { saveToPreferences("telephone", it) }
        Log.i("saveProfil","$clientResponse")
    }

    fun getProfil(): ClientResponse {
        val gson = Gson()
        val jsonCommandes = retrieveFromPreferences("commandes", "")
        val commandes = gson.fromJson(jsonCommandes, Array<Commande>::class.java)?.toList() ?: emptyList()

        return ClientResponse(
            retrieveFromPreferences("email", ""),
            retrieveFromPreferences("nom", ""),
            retrieveFromPreferences("prenom", ""),
            retrieveFromPreferences("adresse", ""),
            retrieveFromPreferences("telephone", "")
        )
    }


    private fun getUserId(): String {
        val storage = Storage(context) // Remplacez 'context' par votre contexte actuel
        return storage.getProfil().email ?: ""
    }



}