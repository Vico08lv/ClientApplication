package com.example.clientapplication.fragment.app

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clientapplication.AppViewModel
import com.example.clientapplication.MainActivity
import com.example.clientapplication.R
import com.example.clientapplication.SecondActivity
import com.example.clientapplication.databinding.FragmentProfileBinding
import com.example.clientapplication.localStorage.Storage
import com.example.clientapplication.model.request.ClientRequest


class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        appViewModel.profil.observe(viewLifecycleOwner, Observer { profil ->
            root.findViewById<TextView>(R.id.profilNom).text = profil.nom
            root.findViewById<TextView>(R.id.profilPrenom).text = profil.prenom
            root.findViewById<TextView>(R.id.profilAdresse).text = profil.adresse
            root.findViewById<TextView>(R.id.profilEmail).text = profil.email
            root.findViewById<TextView>(R.id.profilTelephone).text = profil.telephone
        })

        root.findViewById<Button>(R.id.deconnexion).setOnClickListener {  }

        root.findViewById<LinearLayout>(R.id.modifierProfil).setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.edition_profil)

//            val nomEditText = dialog.findViewById<EditText>(R.id.editionNom)
//            val prenomEditText = dialog.findViewById<EditText>(R.id.editionPrenom)
//            val adresseEditText = dialog.findViewById<EditText>(R.id.editionAdresse)
//            val telephoneEditText = dialog.findViewById<EditText>(R.id.editionTelephone)

            val enregistrerButton = dialog.findViewById<Button>(R.id.enregistrerModif)
            enregistrerButton.setOnClickListener {
                appViewModel.putClient(ClientRequest(
                    dialog.findViewById<EditText>(R.id.editionNom).text.toString(),
                    dialog.findViewById<EditText>(R.id.editionPrenom).text.toString(),
                    dialog.findViewById<EditText>(R.id.editionAdresse).text.toString(),
                    dialog.findViewById<EditText>(R.id.editionTelephone).text.toString())
                )
                dialog.dismiss()
            }


            // Réglage des paramètres de la fenêtre du dialog
            val window = dialog.window
            val layoutParams = window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT // Largeur de la fenêtre
            layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT // Hauteur de la fenêtre
            window?.attributes = layoutParams

            // Afficher le dialogue
            dialog.show()
        }

        val deconnexionButton = root.findViewById<Button>(R.id.deconnexion)
        deconnexionButton.setOnClickListener {

            var store : Storage? = context?.let { it1 -> Storage(it1) }
            if (store != null) {
                store.clear()
            }
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }


            return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}