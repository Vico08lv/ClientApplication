package com.example.clientapplication.fragment.auth


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.clientapplication.R
import com.example.clientapplication.SecondActivity
import com.example.clientapplication.databinding.FragmentLoginBinding
import com.example.clientapplication.databinding.FragmentRegisterBinding
import com.example.clientapplication.model.request.AuthenticationRequest
import com.example.clientapplication.model.request.RegisterRequest
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.messaging.FirebaseMessaging


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding ? = null
    private val binding get() = _binding!!
    private lateinit var registerVM: LoginViewModel
    private var token: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        root.findViewById<TextView>(R.id.dejaUnCompte).setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.setCustomAnimations(
                R.anim.enter_bottom_to_top,
                R.anim.exit_bottom_to_top,
//                R.anim.enter_bottom_to_top,
//                R.anim.exit_bottom_to_top
            )

            val loginFragment = LoginFragment()
            fragmentTransaction.replace(R.id.fragmentContainer, loginFragment)
//            fragmentTransaction.addToBackStack(null)  // Optionnel : ajouter la transaction à la pile de retour
            fragmentTransaction.commit()
        }


        root.findViewById<Button>(R.id.registerButton).setOnClickListener {

            registerVM = ViewModelProvider(this).get(LoginViewModel::class.java)

            val registerRequest = RegisterRequest(
                root.findViewById<EditText>(R.id.registrationUserEditText).text.toString(),
                root.findViewById<EditText>(R.id.registrationEmailEditText).text.toString(),
                root.findViewById<EditText>(R.id.registrationMDPEditText).text.toString(),
            )

            registerVM.enregistrementToApi(registerRequest)

            // Observe changes in status LiveData if needed
            registerVM.status.observe(viewLifecycleOwner) { status ->
                if (status == "200") {
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

                    val loginFragment = LoginFragment()
                    fragmentTransaction.replace(R.id.fragmentContainer, loginFragment)
//            fragmentTransaction.addToBackStack(null)  // Optionnel : ajouter la transaction à la pile de retour
                    fragmentTransaction.commit()
                }
            }
        }

        return root
    }


}