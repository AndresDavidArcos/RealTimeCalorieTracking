package com.app.realtimecalorietracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.app.realtimecalorietracking.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationContainer) as NavHostFragment
        val navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigation, navController)
    }
}