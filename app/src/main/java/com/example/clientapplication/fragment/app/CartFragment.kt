package com.example.clientapplication.fragment.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientapplication.AppViewModel
import com.example.clientapplication.R
import com.example.clientapplication.databinding.FragmentCartBinding
import com.example.clientapplication.model.request.CommandeRequest


class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var appViewModel: AppViewModel
    lateinit var adapter : CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val commandeRecyclerView = root.findViewById<RecyclerView>(R.id.reclycleView_cart)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        commandeRecyclerView.layoutManager = layoutManager
        val emptyCommandes = emptyList<CommandeRequest>()
        adapter = CartAdapter(emptyCommandes, requireContext())
        commandeRecyclerView.adapter = adapter


        /** MAJ liste commandes **/
        appViewModel.panier.observe(viewLifecycleOwner, Observer { panier ->
            panier?.let {
                adapter.updatePanier(panier)
            }
        })

        val panier = appViewModel.getPanier()
        Log.i("Cart","$panier")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}