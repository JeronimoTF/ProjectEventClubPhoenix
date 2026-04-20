package com.example.projecteventclub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projecteventclub.vista_usuario.main.usuarios.activity_usuarioPrincipal
import com.example.projecteventclub.vista_usuario.main.admin.activity_adminPrincipal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        // Obtener el tipo de usuario del Intent
        val userType = intent.getStringExtra("USER_TYPE")

        if (savedInstanceState == null) {
            when (userType) {
                "USER" -> loadFragment(activity_usuarioPrincipal())
                "ADMIN" -> loadFragment(activity_adminPrincipal())
                else -> loadFragment(activity_usuarioPrincipal()) // Por defecto
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}