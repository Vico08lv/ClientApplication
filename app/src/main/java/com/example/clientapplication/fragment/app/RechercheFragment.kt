package com.example.clientapplication.fragment.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.clientapplication.AppViewModel
import com.example.clientapplication.R
import com.example.clientapplication.databinding.FragmentRechercheBinding
import com.example.clientapplication.model.request.ProduitQuantiteRequest
import com.google.android.material.chip.Chip


class RechercheFragment : Fragment() {

    private var _binding: FragmentRechercheBinding? = null
    private val binding get() = _binding!!
    private lateinit var appViewModel : AppViewModel
    lateinit var adapter : ProduitAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val rechercheViewModel = ViewModelProvider(this).get(RechercheViewModel::class.java)
//        val rootView = inflater.inflate(R.layout.fragment_recherche, container, false)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        appViewModel.getAllProduits()

        //lier le fragment
        _binding = FragmentRechercheBinding.inflate(inflater, container, false)
        val rootView : View = binding.root


        val productRecyclerView = rootView.findViewById<RecyclerView>(R.id.reclycleView_produit)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productRecyclerView.layoutManager = layoutManager
        adapter = ProduitAdapter(emptyList(),requireContext(),appViewModel)
        productRecyclerView.adapter = adapter

        /** BARRE DE RECHERCHE **/

        val searchView = rootView.findViewById<SearchView>(R.id.rechercheSearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("Recherche", "Texte : $query")
                    appViewModel.updateSearchBarContent(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    appViewModel.updateSearchBarContent(newText)
                }
                return true
            }
        })


        /** Filtrage **/

        //Initialisation des filtres cochés
//        appViewModel.addChip("LEGUME")
//        appViewModel.addChip("FRUIT")
//        appViewModel.addChip("VIANDE")
//        appViewModel.addChip("POISSON")
//        appViewModel.addChip("MIEL")
//        appViewModel.addChip("AUTRES")

        val chipLegume = rootView.findViewById<Chip>(R.id.chip_legume)
        val chipFruit = rootView.findViewById<Chip>(R.id.chip_fruit)
        val chipViande = rootView.findViewById<Chip>(R.id.chip_viande)
        val chipPoisson = rootView.findViewById<Chip>(R.id.chip_poisson)
        val chipMiel = rootView.findViewById<Chip>(R.id.chip_miel)
        val chipAutre = rootView.findViewById<Chip>(R.id.chip_autre)

        chipLegume.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("LEGUME") // Ajouter le chip à la liste si coché
            } else {
                appViewModel.removeChip("LEGUME") // Retirer le chip de la liste s'il est décoché
            }
        }

        chipFruit.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("FRUIT")
            } else {
                appViewModel.removeChip("FRUIT")
            }
        }

        chipViande.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("VIANDE")
            } else {
                appViewModel.removeChip("VIANDE")
            }
        }

        chipPoisson.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("POISSON")
            } else {
                appViewModel.removeChip("POISSON")
            }
        }

        chipMiel.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("MIEL")
            } else {
                appViewModel.removeChip("MIEL")
            }
        }

        chipAutre.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked) {
                appViewModel.addChip("AUTRES")
            } else {
                appViewModel.removeChip("AUTRES")
            }
        }



        /** MAJ liste produits **/
        appViewModel.produits.observe(viewLifecycleOwner, Observer { produits ->
            val selectedChips = appViewModel.selectedChips.value ?: return@Observer
            val searchBarContent = appViewModel.searchBarContent.value

            val produitsFiltres = produits.filter { produit ->
                val matchCategory = selectedChips.contains(produit.categorie.toString())
                val matchSearch = searchBarContent?.let {
                    if (it.isNotEmpty()) {
                        produit.nom?.contains(it, ignoreCase = true) ?: false
                    } else {
                        true // Ne pas appliquer de filtre si searchBarContent est une chaîne vide
                    }
                } ?: true // Ne pas appliquer de filtre si searchBarContent est null

                matchCategory && matchSearch && !produit.delete!!
            }

            adapter.updateProducts(produitsFiltres)
        })

        appViewModel.selectedChips.observe(viewLifecycleOwner, Observer { newSelectedChips ->
            val produits = appViewModel.produits.value ?: return@Observer
            val searchBarContent = appViewModel.searchBarContent.value

            val produitsFiltres = produits.filter { produit ->
                val matchCategory = newSelectedChips.contains(produit.categorie.toString())
                val matchSearch = searchBarContent?.let {
                    if (it.isNotEmpty()) {
                        produit.nom?.contains(it, ignoreCase = true) ?: false
                    } else {
                        true // Ne pas appliquer de filtre si searchBarContent est une chaîne vide
                    }
                } ?: true // Ne pas appliquer de filtre si searchBarContent est null

                matchCategory && matchSearch && !produit.delete!!
            }

            adapter.updateProducts(produitsFiltres)
        })

        appViewModel.searchBarContent.observe(viewLifecycleOwner, Observer { searchText ->
            val produits = appViewModel.produits.value ?: return@Observer
            val selectedChips = appViewModel.selectedChips.value ?: return@Observer

            val produitsFiltres = produits.filter { produit ->
                val matchCategory = selectedChips.contains(produit.categorie.toString())
                val matchSearch = searchText?.let {
                    if (it.isNotEmpty()) {
                        produit.nom?.contains(it, ignoreCase = true) ?: false
                    } else {
                        true // Ne pas appliquer de filtre si searchBarContent est une chaîne vide
                    }
                } ?: true // Ne pas appliquer de filtre si searchBarContent est null

                matchCategory && matchSearch && !produit.delete!!
            }

            adapter.updateProducts(produitsFiltres)
        })





        return rootView
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("RechercheFragment", "RechercheFragment destroyed!")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}