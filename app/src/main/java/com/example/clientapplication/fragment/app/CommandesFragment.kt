package com.example.clientapplication.fragment.app

import android.os.Bundle
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
import com.example.clientapplication.databinding.FragmentCommandesBinding
import com.example.clientapplication.model.response.CommandesResponse

class CommandesFragment : Fragment() {

    private var _binding: FragmentCommandesBinding? = null
    private val binding get() = _binding!!
    private lateinit var appViewModel: AppViewModel
    lateinit var adapter : CommandeAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        appViewModel.getCommandes()


        _binding = FragmentCommandesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val commandeRecyclerView = root.findViewById<RecyclerView>(R.id.reclycleView_commandes)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        commandeRecyclerView.layoutManager = layoutManager
        val emptyCommandes = CommandesResponse(0, emptyList())
        adapter = CommandeAdapter(emptyCommandes, requireContext())
        commandeRecyclerView.adapter = adapter

        appViewModel.commandes.observe(viewLifecycleOwner, Observer { commandes ->
            adapter.updateCommandes(commandes)

            appViewModel.insertCommande(commandes)
        })


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}