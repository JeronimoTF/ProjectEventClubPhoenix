package com.example.projecteventclub

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.projecteventclub.atributos.comida.Activity_comidas
import com.example.projecteventclub.atributos.experiencia.inicio.homeEventFragment
import com.example.projecteventclub.atributos.pefil.EditarPerfilUsuarioFragment
import com.example.projecteventclub.atributos.pefil.PerfilFragment
import com.example.projecteventclub.atributos.pefil.configuraciones.activity_data_modifcation
import com.example.projecteventclub.vista_usuario.main.usuarios.activity_usuarioPrincipal
import com.example.projecteventclub.vista_usuario.main.admin.activity_adminPrincipal
import com.example.projecteventclub.vista_usuario.main.usuarios.fragment_configUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)

        configurarMenuPorRol(navView.menu)

        cargarFragment(homeEventFragment())
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> cargarFragment(homeEventFragment())
                R.id.nav_perfilusuario -> cargarFragment(PerfilFragment())
            }
            true
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> cargarFragment(homeEventFragment())
                R.id.nav_comida -> cargarFragment(Activity_comidas())
                R.id.nav_anfitrion -> cargarFragment(activity_adminPrincipal())
                R.id.nav_enventosusuario -> cargarFragment(activity_usuarioPrincipal())
                R.id.nav_perfilusuario -> cargarFragment(PerfilFragment())
                R.id.nav_editarUsuario -> cargarFragment(EditarPerfilUsuarioFragment())
                R.id.nav_logout -> cerrarSesion()
            }
            drawerLayout.closeDrawers()
            true
        }
    } //Cierre  de onCreate

    private fun configurarMenuPorRol(menu: Menu) {

        // Aqui va la Lógica de roles
    }

    private fun cerrarSesion() {
        // Por ahora vacío, sin base de datos
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}