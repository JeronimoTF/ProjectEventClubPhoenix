package com.example.projecteventclub

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projecteventclub.atributos.comida.Activity_comidas
import com.example.projecteventclub.atributos.experiencia.inicio.homeEventFragment
import com.example.projecteventclub.atributos.pefil.EditarPerfilUsuarioFragment
import com.example.projecteventclub.atributos.pefil.PerfilFragment
import com.example.projecteventclub.auth.login.Login
import com.example.projecteventclub.models.UserProfile
import com.example.projecteventclub.vista_usuario.main.admin.activity_adminPrincipal
import com.example.projecteventclub.vista_usuario.main.anfitrion.FragmentMainAnfitrion
import com.example.projecteventclub.vista_usuario.main.usuarios.activity_usuarioPrincipal
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var ivFotoToolbar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navView = findViewById<NavigationView>(R.id.nav_view)
        
        // Vinculamos la imagen de la barra superior con el ID correcto
        ivFotoToolbar = findViewById(R.id.iv_perfil_toolbar)

        setSupportActionBar(toolbar)
        // ✅ Desactivamos el título por defecto del sistema para evitar que se repita
        // Y nos aseguramos de que el título esté vacío
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            title = ""
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)

        // Cargamos el perfil inicial (rol y foto)
        obtenerPerfilYConfigurarInterfaz()

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
                R.id.nav_anfitrion -> cargarFragment(FragmentMainAnfitrion())
                R.id.nav_enventosusuario -> cargarFragment(activity_usuarioPrincipal())
                R.id.nav_perfilusuario -> cargarFragment(PerfilFragment())
                R.id.nav_editarUsuario -> cargarFragment(EditarPerfilUsuarioFragment())
                R.id.nav_administrador -> cargarFragment(activity_adminPrincipal())
                R.id.nav_logout -> cerrarSesion()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    /**
     * Permite a los fragmentos solicitar una actualización de la interfaz (como la foto)
     */
    fun actualizarInterfaz() {
        obtenerPerfilYConfigurarInterfaz()
    }

    private fun obtenerPerfilYConfigurarInterfaz() {
        val user = SupaBaseClient.client.auth.currentUserOrNull() ?: return
        val userId = user.id

        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["perfiles"]
                        .select { filter { eq("id", userId) } }
                        .decodeSingleOrNull<UserProfile>()
                }
                
                withContext(Dispatchers.Main) {
                    if (profile != null) {
                        configurarMenuPorRol(navView.menu, profile.rol ?: "USER")
                        
                        // Cargamos la foto en la barra superior si existe
                        if (!profile.avatar_url.isNullOrEmpty()) {
                            ivFotoToolbar.load(profile.avatar_url) {
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.ic_user)
                                error(R.drawable.ic_user)
                            }
                        }
                    } else {
                        configurarMenuPorRol(navView.menu, "USER")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun configurarMenuPorRol(menu: Menu, rol: String) {
        val upperRol = rol.uppercase()

        menu.findItem(R.id.nav_home)?.isVisible = true
        menu.findItem(R.id.nav_perfilusuario)?.isVisible = true
        menu.findItem(R.id.nav_editarUsuario)?.isVisible = true
        menu.findItem(R.id.nav_logout)?.isVisible = true

        when (upperRol) {
            "ADMIN" -> {
                menu.findItem(R.id.nav_administrador)?.isVisible = true
                menu.findItem(R.id.nav_anfitrion)?.isVisible = false
                menu.findItem(R.id.nav_enventosusuario)?.isVisible = false
                menu.findItem(R.id.nav_comida)?.isVisible = false
            }
            "ANFITRION" -> {
                menu.findItem(R.id.nav_administrador)?.isVisible = false
                menu.findItem(R.id.nav_anfitrion)?.isVisible = true
                menu.findItem(R.id.nav_enventosusuario)?.isVisible = false
                menu.findItem(R.id.nav_comida)?.isVisible = false
            }
            else -> {
                menu.findItem(R.id.nav_administrador)?.isVisible = false
                menu.findItem(R.id.nav_anfitrion)?.isVisible = false
                menu.findItem(R.id.nav_enventosusuario)?.isVisible = true
                menu.findItem(R.id.nav_comida)?.isVisible = true
            }
        }
    }

    private fun cerrarSesion() {
        lifecycleScope.launch {
            try {
                SupaBaseClient.client.auth.signOut()
                val intent = Intent(this@MainActivity, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}