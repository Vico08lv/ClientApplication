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

    // Ajouter un produit au panier
    fun addToCart(produit: ProduitQuantiteRequest, producteur: String) {
        val gson = Gson()
        val jsonProduits = retrieveFromPreferences("panier", "")
        val commandes: MutableMap<String, MutableList<CommandeRequest>> = gson.fromJson(
            jsonProduits,
            object : TypeToken<MutableMap<String, MutableList<CommandeRequest>>>() {}.type
        ) ?: mutableMapOf()

        val commandeProducteur = commandes[producteur]

        if (commandeProducteur != null) {
            // Recherchez une commande existante pour ce producteur et ajoutez le produit si trouvé
            val existingCommande = commandeProducteur.firstOrNull()
            val existingProduct = existingCommande?.produits?.find { it.id == produit.id }

            if (existingProduct != null) {
                existingProduct.quantite = (existingProduct.quantite ?: 0) + (produit.quantite ?: 0)
            } else {
                existingCommande?.produits = existingCommande?.produits?.toMutableList()
                (existingCommande?.produits as MutableList<ProduitQuantiteRequest>?)?.add(produit)
            }
        } else {
            // Si aucune commande pour ce producteur n'existe, créez une nouvelle commande
            val newCommande = CommandeRequest(
                id = 0,
                status = StatusCommande.EN_ATTENTE_DE_VALIDATION,
                cliend_id = getUserId(),
                produits = mutableListOf(produit),
                date = Date()
            )
            commandes[producteur] = mutableListOf(newCommande)
        }

        val jsonUpdatedPanier = gson.toJson(commandes)
        saveToPreferences("panier", jsonUpdatedPanier)


        Log.d("Panier", "Objets du panier après ajout : $jsonUpdatedPanier")
    }




    private fun getUserId(): String {
        val storage = Storage(context) // Remplacez 'context' par votre contexte actuel
        return storage.getProfil().email ?: ""
    }



    // Obtenir les produits du panier
    fun getCart(): List<ProduitQuantiteResponse> {
        val gson = Gson()
        val jsonProduits = retrieveFromPreferences("panier", "")
        return gson.fromJson(jsonProduits, object : TypeToken<List<ProduitQuantiteResponse>>() {}.type)
            ?: emptyList()
    }





    // Vider le panier
    fun clearCart() {
        saveToPreferences("panier", "")
    }


}