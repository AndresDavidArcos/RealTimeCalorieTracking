package com.app.realtimecalorietracking.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.app.realtimecalorietracking.MainActivity
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentHomeBinding
import com.app.realtimecalorietracking.databinding.FragmentProfileBinding
import com.app.realtimecalorietracking.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        controladores()
        return binding.root
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