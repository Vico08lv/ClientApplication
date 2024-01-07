package com.example.clientapplication.fragment.auth


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.IOException
import java.util.Locale


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding ? = null
    private val binding get() = _binding!!
    private lateinit var connexionVM: LoginViewModel
    private var token: String = ""
    private lateinit var store : Storage

    val PERMISSION_REQUEST_CODE = 0



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

        displayCountryName(root)



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun displayCountryName(root : View) {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val geocoder = Geocoder(requireContext(), Locale.getDefault())

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocationPermission) {
            // Demander la permission d'accès à la localisation si elle n'est pas accordée
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        locationManager.allProviders.forEach { provider ->
            val location: Location? = locationManager.getLastKnownLocation(provider)
            location?.let {
                try {
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val countryName = addresses[0].countryName
                        root.findViewById<TextView>(R.id.paysloc).text = countryName
                        return@forEach
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
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