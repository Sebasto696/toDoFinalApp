package com.example.myapplication.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        //Se utiliza la clase Handler para posponer la navegación a otra actividad o fragmento
        // durante dos segundos mediante la expresión Runnable que verifica si hay un usuario autenticado o no
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (auth.currentUser != null){
                navController.navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                navController.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        },2000)

    }
}