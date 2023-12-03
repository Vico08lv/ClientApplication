package com.example.clientapplication.network


import com.example.clientapplication.model.request.AuthenticationRequest
import com.example.clientapplication.model.request.ClientRequest
import com.example.clientapplication.model.request.CommandeRequest
import com.example.clientapplication.model.request.RegisterRequest
import com.example.clientapplication.model.response.ClientResponse
import com.example.clientapplication.model.response.CommandeResponse
import com.example.clientapplication.model.response.CommandesResponse
import com.example.clientapplication.model.response.ConnexionResponse
import com.example.clientapplication.model.response.ProduitsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @GET("/api/client/commandes")
    suspend fun afficherCommandes() : CommandesResponse
    @GET("/api/produit/categorie/{categorie}")
    suspend fun rechercherProduits() : ProduitsResponse
    @GET("/api/produit/all")
    suspend fun rechercherProduitsAll() : ProduitsResponse
    @GET("/api/client/profil")
    suspend fun profilClient() : ClientResponse
    @POST("/api/client/commander")
    suspend fun passerCommande(@Body commandeRequest: CommandeRequest): CommandeResponse
    @POST("/api/auth/client/authenticate")
    suspend fun connexion(@Body authenticationRequest: AuthenticationRequest): ConnexionResponse
    @POST("/api/auth/client/register")
    suspend fun enregistrement(@Body registerRequest: RegisterRequest): ConnexionResponse
    @PUT("/api/client/profil")
    suspend fun modifierProfil(@Body clientRequest: ClientRequest) : ClientResponse

}