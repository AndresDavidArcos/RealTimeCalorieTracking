package com.app.realtimecalorietracking.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.realtimecalorietracking.R

class CalorieAlertDialog(private val excessCalories: Float) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_calorie_alert, container, false)

        val btnCloseAlert: Button = view.findViewById(R.id.btn_close_alert)
        val tvCalorieDetail: TextView = view.findViewById(R.id.tv_calorie_detail)

        tvCalorieDetail.text = "Has consumido $excessCalories calorías de más hoy."

        btnCloseAlert.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
