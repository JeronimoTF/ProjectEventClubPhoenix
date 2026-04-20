package com.example.projecteventclub.auth.registro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


class RegistroDatos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_datos)

        val btnSig1: Button = findViewById(R.id.btnSiguiente)

        btnSig1.setOnClickListener {
            val intent = Intent(this@RegistroDatos, RegistroLocalizacionUsu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
