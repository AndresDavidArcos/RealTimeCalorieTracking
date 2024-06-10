package com.app.realtimecalorietracking
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.app.realtimecalorietracking.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context


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
        val email = intent.getStringExtra("email")
        val sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.apply()

    }
}