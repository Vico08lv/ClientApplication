package com.example.clientapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.clientapplication.R
import com.example.clientapplication.firebase.FirebaseService
import com.example.clientapplication.fragment.auth.LoginFragment
import com.example.clientapplication.localStorage.Storage
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var store : Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        val loginFragmentManager: FragmentManager = supportFragmentManager
        val loginFragmentTransaction: FragmentTransaction = loginFragmentManager.beginTransaction()
        val loginFragment = LoginFragment()

        loginFragmentTransaction.add(R.id.fragmentContainer, loginFragment)

        loginFragmentTransaction.commit()



        store= Storage(getApplication())
        val token : String = store.getToken()



        if (token != "" && !store.isExpired())
        {
            startActivity(Intent(this, SecondActivity::class.java))
            this.finish()
        }else
        {
            store.clear()
        }
    }
}
