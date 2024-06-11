package com.app.realtimecalorietracking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.realtimecalorietracking.adapter.CaloriesItem
import com.app.realtimecalorietracking.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

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

    fun calculateCaloriesForToday(items: List<CaloriesItem>): Int {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val regex = Regex("[^0-9]+") // Expresión regular para eliminar caracteres que no sean números

        return items.filter { it.fecha == currentDate }
            .sumBy {
                val calories = it.calorias.replace(regex, "") // Eliminar caracteres no numéricos
                calories.toIntOrNull() ?: 0
            }
    }

    fun filterRecentItems(items: List<CaloriesItem>, days: Int): List<CaloriesItem> {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        val thresholdDate = Calendar.getInstance().apply {
            time = currentDate
            add(Calendar.DAY_OF_YEAR, -(days + 1))
        }.time

        return items.filter {
            val itemDate = sdf.parse(it.fecha)
            itemDate != null && !itemDate.before(thresholdDate)
        }
    }

    fun getTodayCaloriesItems(): MutableList<CaloriesItem> {
        // Esto es solo un ejemplo. Aquí deberías implementar el código para obtener los ítems de calorías del día actual.
        // Por ejemplo, podrías obtenerlos desde una base de datos o una lista almacenada localmente.
        val items = mutableListOf(
            CaloriesItem("Spaguettis", "300 Cal", "10-06-2024"),
            CaloriesItem("Helado", "100 Cal", "10-06-2024"),
            CaloriesItem("Ensalada", "200 Cal", "10-06-2024"),
            CaloriesItem("Pizza", "400 Cal", "10-06-2024"),
            CaloriesItem("Sushi", "300 Cal", "10-06-2024"),
            CaloriesItem("Hamburguesa", "500 Cal", "10-06-2024")
        )
        return items
    }
}