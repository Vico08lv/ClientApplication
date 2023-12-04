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


        /** MAJ liste produits **/
        appViewModel.produits.observe(viewLifecycleOwner, Observer { produits ->
            adapter.updateProducts(produits)
        })


        /** Filtrage **/
        /**
         *
         */

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