package com.app.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentHomeBinding
import com.app.realtimecalorietracking.databinding.FragmentProfileBinding
import com.app.realtimecalorietracking.model.User
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var receivedUser: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}
