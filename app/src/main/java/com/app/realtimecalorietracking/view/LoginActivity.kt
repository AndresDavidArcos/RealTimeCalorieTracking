package com.app.realtimecalorietracking.view

import android.view.View

import com.app.realtimecalorietracking.R

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.app.realtimecalorietracking.MainActivity
import com.app.realtimecalorietracking.databinding.ActivityLoginBinding
import com.app.realtimecalorietracking.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    var errorVar = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()
        controladores()
        binding.btnLogin.isEnabled = false
        binding.btnLogin.setTextColor(Color.GRAY)
        binding.tvRegister.setTextColor(Color.parseColor("#9EA1A1"))
        binding.tvRegister.isEnabled = false
    }
    private fun controladores(){
        minPass()
        loginButton()
    }

    private fun loginButton() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica si ambos campos están llenos y habilita o deshabilita el botón en consecuencia
                checkFieldsAndEnableButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Verifica si ambos campos están llenos y habilita o deshabilita el botón en consecuencia
                checkFieldsAndEnableButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    fun checkFieldsAndEnableButton() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPass.text.toString()

        // Habilita el botón si ambos campos están llenos, de lo contrario, deshabilítalo
        binding.btnLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        binding.tvRegister.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        binding.tvRegister.setTextColor(Color.parseColor("#FFFFFFFF"))
        if (binding.btnLogin.isEnabled){

            binding.btnLogin.setTextColor(Color.WHITE)
        }else{

            binding.btnLogin.setTextColor(Color.GRAY)
        }
    }


    private fun minPass(){
        val pass = binding.tilPass
        binding.etPass.addTextChangedListener {
            if (errorVar && binding.etPass.text.toString().length > 5){
                pass.error = null
                errorVar = false
                pass.boxStrokeColor = resources.getColor(R.color.white)
//                Toast.makeText(this, "quitar?", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        loginViewModel.registerUser(email,pass) { isRegister ->
            if (isRegister) {
                goToHome(email)
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun goToHome(email: String?){
        val intent = Intent (this, MainActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }
    private fun loginUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        if (pass.length > 5){
            loginViewModel.loginUser(email,pass){ isLogin ->
                if (isLogin){
                    sharedPreferences.edit().putString("email",email).apply()
                    if(intent.extras?.containsKey("OnLoginRedirectToWidget") == true){
                        goToHome(email)
                        moveTaskToBack(true)
                    }else{
                        goToHome(email)
                    }

                }else {
                    Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            val pass = binding.tilPass
            pass.error = "Mínimo 6 dígitos"
//            Toast.makeText(this, "666", Toast.LENGTH_SHORT).show()
            errorVar = true
        }

    }
    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        loginViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome(email)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}