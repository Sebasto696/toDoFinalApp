package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

//Este fragmento extiende la clase Fragment, que es donde esta realizada toda la vista
class SignInFragment : Fragment() {

    //Se utiliza para navegar entre los fragmentos en la aplicación.
    private lateinit var navController: NavController
    //Se utiliza para autenticar a los usuarios a través de Firebase Authentication.
    private lateinit var mAuth: FirebaseAuth
    //Se utiliza para acceder a las vistas del fragmento a través de la técnica de enlace de datos.
    private lateinit var binding: FragmentSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        //Cuando se hace clic en esta vista, se navega a otro fragmento llamado signUpFragment.
        binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        //cuando se hace clic en esta vista, se recupera el correo electrónico
        // y la contraseña del usuario de dos vistas de edición de texto
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            //Luego, si los campos de correo electrónico y contraseña no están vacíos, se llama a la
            // función loginUser() con los valores de correo electrónico y contraseña como argumentos.

            if (email.isNotEmpty() && pass.isNotEmpty())

                loginUser(email, pass)
            else
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    //se llama para autenticar al usuario utilizando la dirección de correo electrónico y la contraseña proporcionadas.
    // Si la autenticación es exitosa, se navega a otro fragmento llamado homeFragment.
    private fun loginUser(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful)
                navController.navigate(R.id.action_signInFragment_to_homeFragment)
            else
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }

}