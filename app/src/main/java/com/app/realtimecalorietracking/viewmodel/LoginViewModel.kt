package com.app.realtimecalorietracking.viewmodel

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.realtimecalorietracking.model.Food
import com.app.realtimecalorietracking.model.Goals
import com.app.realtimecalorietracking.repository.LoginRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    //registerUser se comunica con el repository
    fun registerUser(email: String, pass: String, isRegister: (Boolean) -> Unit) {
        repository.registerUser(email, pass) { response ->
            isRegister(response)
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isLogin(true)
                    } else {
                        isLogin(false)
                    }
                }
        } else {
            isLogin(false)
        }
    }

    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        if (email != null) {
            isEnableView(true)
        } else {
            isEnableView(false)
        }
    }


    fun calculateCaloriesForToday(items: MutableList<Food>): Int {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        return items.filter { sdf.format(it.date) == currentDate }
            .sumOf { it.calories }
    }

    fun filterRecentItems(items: List<Food>, days: Int): MutableList<Food> {
        val currentDate = Date()
        val thresholdDate = Calendar.getInstance().apply {
            time = currentDate
            add(Calendar.DAY_OF_YEAR, -days)
        }.time

        return items.filter {
            it.date.after(thresholdDate)
        }.toMutableList()
    }

    fun getTodayCaloriesItems(): MutableList<Food> {
        val foodsList = mutableListOf<Food>()

        db.collection("foods")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val food = document.toObject(Food::class.java)
                    foodsList.add(food)
                }
            }
        return foodsList
    }
}