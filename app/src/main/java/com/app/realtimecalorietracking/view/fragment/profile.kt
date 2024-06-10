package com.app.realtimecalorietracking.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.app.realtimecalorietracking.MainActivity
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentProfileBinding
import com.app.realtimecalorietracking.model.User
import com.app.realtimecalorietracking.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val db = FirebaseFirestore.getInstance()
    private lateinit var receivedUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        controladores()
        
        val spinnerGeneros: Spinner = binding.spinnerGenero
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genero_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGeneros.adapter = adapter
        }

        // Configurar listener para las selecciones del spinner
        spinnerGeneros.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Obtener el ítem seleccionado usando position
                val selectedGenero = parent.getItemAtPosition(position).toString()
                // Aquí puedes manejar la selección del usuario
                Toast.makeText(
                    requireContext(),
                    "Seleccionado: $selectedGenero",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Código a ejecutar si no se selecciona nada
            }
        }

        val spinnerActividades: Spinner = binding.spinnerActividad
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.actividad_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerActividades.adapter = adapter
        }

        // Configurar listener para las selecciones del spinner
        spinnerActividades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Obtener el ítem seleccionado usando position
                val selectedActividad = parent.getItemAtPosition(position).toString()
                // Aquí puedes manejar la selección del usuario
                Toast.makeText(
                    requireContext(),
                    "Seleccionado: $selectedActividad",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Código a ejecutar si no se selecciona nada
            }
        }
    }

    private fun logOut() {
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        (requireActivity() as MainActivity).apply {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun controladores() {
        val btnLogOut: ImageView = binding.root.findViewById(R.id.btnLogOut)
        btnLogOut.setOnClickListener {
            it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                //activity?.onBackPressedDispatcher?.onBackPressed()
                logOut()
            }.start()
        }
    }
}
