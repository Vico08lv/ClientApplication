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
import com.example.clientapplication.localStorage.Storage
import com.example.clientapplication.model.request.AuthenticationRequest
import com.google.firebase.messaging.FirebaseMessaging


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding ? = null
    private val binding get() = _binding!!
    private lateinit var connexionVM: LoginViewModel
    private var token: String = ""
    private lateinit var store : Storage


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        store = Storage(requireContext())


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        root.findViewById<TextView>(R.id.pasDeCompte).setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.setCustomAnimations(
                R.anim.enter_bottom_to_top,
                R.anim.exit_bottom_to_top,
//                R.anim.enter_bottom_to_top,
//                R.anim.exit_bottom_to_top
            )

            val registerFragment = RegisterFragment()
            fragmentTransaction.replace(R.id.fragmentContainer, registerFragment)

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            fragmentTransaction.addToBackStack(null)  // Optionnel : ajouter la transaction à la pile de retour
            fragmentTransaction.commit()
        }


        root.findViewById<Button>(R.id.connexionButton).setOnClickListener {


            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result

                    connexionVM = ViewModelProvider(this).get(LoginViewModel::class.java)

                    val authRequest = AuthenticationRequest(
                        root.findViewById<EditText>(R.id.connexionEmailEditText).text.toString(),
                        root.findViewById<EditText>(R.id.connexionMDPEditText).text.toString(),
                        token
                    )

                    connexionVM.connexionToApi(authRequest)

                    // Observe changes in status LiveData if needed
                    connexionVM.status.observe(viewLifecycleOwner) { status ->

                        if (status == "200") {
                            startActivity(Intent(requireActivity(), SecondActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}


//class LoginFragment : Fragment() {
//
//    private lateinit var txtEmail: EditText
//    private lateinit var txtPassword: EditText
//    private lateinit var btnConnection: Button
//    private lateinit var pasDeCompte: TextView
//
//    @Override
//    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//
//
//        txtEmail = view.findViewById(R.id.connexionEmailEditText)
//
//        txtPassword = view.findViewById(R.id.connexionMDPEditText)
//
//        btnConnection = view.findViewById(R.id.connexionButton)
//
//        pasDeCompte = view.findViewById(R.id.pasDeCompte)
//        pasDeCompte.setOnClickListener {
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//
//
//            fragmentTransaction.setCustomAnimations(
//                R.anim.enter_bottom_to_top,
//                R.anim.exit_bottom_to_top,
////                R.anim.enter_bottom_to_top,
////                R.anim.exit_bottom_to_top
//            )
//
//
//            val registerFragment = RegisterFragment()
//            fragmentTransaction.replace(R.id.fragmentContainer, registerFragment)
//
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
////            fragmentTransaction.addToBackStack(null)  // Optionnel : ajouter la transaction à la pile de retour
//            fragmentTransaction.commit()
//        }
//
//        return view
//    }
//
//
//}