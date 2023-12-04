package com.example.clientapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.clientapplication.localStorage.Storage
import com.example.clientapplication.model.StatusCommande
import com.example.clientapplication.model.request.ClientRequest
import com.example.clientapplication.model.request.CommandeRequest
import com.example.clientapplication.model.request.ProduitQuantiteRequest
import com.example.clientapplication.model.response.ClientResponse
import com.example.clientapplication.model.response.CommandesResponse
import com.example.clientapplication.model.response.ProduitResponse
import com.example.clientapplication.network.ApiClient
import kotlinx.coroutines.launch
import java.util.Date


class AppViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var store : Storage

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    /** PROFIL **/
    private val _profil = MutableLiveData<ClientResponse>()
    val profil: LiveData<ClientResponse> = _profil
    // Si sur un autre Thread
    fun updateProfil(p: ClientResponse) { _profil.postValue(p)}
    // Si sur le thread principal
    fun setProfil(p: ClientResponse) { _profil.value = p }


    /** COMMANDES **/
    private val _commandes = MutableLiveData<CommandesResponse>()
    val commandes: LiveData<CommandesResponse> = _commandes
    // Si sur un autre Thread
    fun updateCommandes(c: CommandesResponse) { _commandes.postValue(c)}
    // Si sur le thread principal
    fun setCommandes(c: CommandesResponse) { _commandes.value = c }


    /** PRODUITS **/
    private val _produits = MutableLiveData<List<ProduitResponse>>()
    val produits: LiveData<List<ProduitResponse>> = _produits
    fun updateProduits(c: List<ProduitResponse>) { _produits.postValue(c)}
    // Si sur le thread principal
    fun setProduits(c: List<ProduitResponse>) { _produits.value = c }


    /** CART **/
    private val _panier = MutableLiveData<List<CommandeRequest>>()
    val panier: LiveData<List<CommandeRequest>> =  _panier
    fun updatePanier(c: List<CommandeRequest>) { _panier.postValue(c)}
    fun setPanier(c: List<CommandeRequest>) { _panier.value = c}

    fun getPanier(): List<CommandeRequest>? {
        Log.i("Panier","${panier.value}")
        return _panier.value
    }

    fun clearPanier(){
        _panier.value = emptyList()
    }

    fun addToCart(produit: ProduitQuantiteRequest) {
        val currentPanier = _panier.value?.toMutableList() ?: mutableListOf()

        val user_id = store.retrieveFromPreferences("email","")

        val newCommande = CommandeRequest(
//            id = 0,
//            status = StatusCommande.EN_ATTENTE_DE_VALIDATION,
//            cliend_id = user_id,
            produits = mutableListOf(produit),
//            date = Date()
        )

        currentPanier.add(newCommande)
        updatePanier(currentPanier)
    }







    private val apiClient: ApiClient by lazy {ApiClient(getApplication())}
    private val apiService by lazy {apiClient.apiService}




    /** RECUPERER LES COMMANDES DE L'UTILISATEUR **/
    fun getCommandes() {
        viewModelScope.launch {
            try {
                val response = apiService.afficherCommandes()
                response.commandes?.let { commandesList ->
                    val commandesResponse = CommandesResponse(commandes = commandesList)
                    updateCommandes(commandesResponse)
                }
                Log.d("GET:/api/client/commandes", response.toString())
            } catch (e: Exception) {
                Log.e("GET:/api/client/commandes", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity

            }
        }
    }
    /** RECUPERER LE PROFIL DE L'UTILISATEUR **/
    fun getClient() {
        viewModelScope.launch {
            try {
                val response = apiService.profilClient()
                updateProfil(response)
                store.saveProfil(response)
                Log.d("GET::/api/client/profil", response.toString())
            } catch (e: Exception) {
                Log.e("GET::/api/client/profil", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity
            }
        }
    }

    /** RECUPERER LES PRODUITS **/
    fun getProduits(){
        viewModelScope.launch {
            try {
                val response = apiService.rechercherProduits()
                updateProduits(response.produits)
                Log.d("GET:/api/produits?categorie={categorie}&date={date}&prix={prix}", response.toString())
            } catch (e: Exception) {
                Log.e("GET::/api/produits?categorie={categorie}&date={date}&prix={prix}", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity
            }
        }
    }

    fun getAllProduits(){
        viewModelScope.launch {
            try {
                val response = apiService.rechercherProduitsAll()
                updateProduits(response.produits)
                Log.d("GET:/api/produits/all", response.toString())
            } catch (e: Exception) {
                Log.e("GET::/api/produits/all", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity
            }
        }
    }



    /** MODIFIER LE PROFIL DE L'UTILISATEUR **/
    fun putClient(clientRequest: ClientRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.modifierProfil(clientRequest)
                updateProfil(response)
                store.saveProfil(response)
                Log.d("PUT::/api/client/profil", response.toString())
                println("#####$response")
            } catch (e: Exception) {
                Log.e("PUT::/api/client/profil", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity
            }
        }
    }

    /** PASSER UNE COMMANDE **/
    fun postCommande(commandeRequest: CommandeRequest){
        viewModelScope.launch {
            try {
//                var commandeRequestSend = commandeRequest
//                commandeRequestSend.cliend_id = store.retrieveFromPreferences("email","")
                val response = apiService.passerCommande(commandeRequest)
                Log.d("POST::/api/client/commander", response.toString())
                getCommandes()
            } catch (e: Exception) {
                Log.e("POST::/api/client/commander", e.message.toString())
                store.clear()
                _navigationEvent.value = NavigationEvent.LaunchNewActivity

            }
        }
    }


    /** AU CHARGEMENT DE L'ACTIVITE **/
    init {
        store= Storage(getApplication())
        getClient()
        getCommandes()
        getAllProduits()
    }




    sealed class NavigationEvent {
        object LaunchNewActivity : NavigationEvent()
    }


}