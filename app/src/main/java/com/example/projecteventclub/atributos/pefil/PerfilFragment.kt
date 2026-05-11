package com.example.projecteventclub.atributos.pefil

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.UserProfile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilFragment : Fragment() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var tvNombre: TextView
    private lateinit var tvRol: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var btnEditar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializar vistas
        ivFotoPerfil = view.findViewById(R.id.iv_foto_perfil)
        tvNombre = view.findViewById(R.id.tv_perfil_nombre)
        tvRol = view.findViewById(R.id.tv_perfil_rol)
        tvCorreo = view.findViewById(R.id.tv_perfil_correo)
        btnEditar = view.findViewById(R.id.btn_editar_perfil)

        // Botón para ir a editar perfil
        btnEditar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditarPerfilUsuarioFragment())
                .addToBackStack(null)
                .commit()
        }

        // Cargar datos del usuario
        obtenerDatosPerfil()

        return view
    }

    private fun obtenerDatosPerfil() {
        val user = SupaBaseClient.client.auth.currentUserOrNull() ?: return
        val userId = user.id

        lifecycleScope.launch {
            try {
                // Consultamos el perfil en la tabla de Supabase
                val profile = withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["perfiles"]
                        .select {
                            filter { eq("id", userId) }
                        }.decodeSingleOrNull<UserProfile>()
                }

                profile?.let {
                    withContext(Dispatchers.Main) {
                        // Concatenamos nombres y apellidos
                        val nombreCompleto = "${it.nombres ?: ""} ${it.apellidos ?: ""}".trim()
                        tvNombre.text = if (nombreCompleto.isNotEmpty()) nombreCompleto else "Usuario sin nombre"
                        
                        // Mostramos el rol (ADMIN o USER)
                        tvRol.text = it.rol ?: "USER"
                        
                        // Mostramos el correo
                        tvCorreo.text = it.correo ?: user.email ?: "Sin correo"

                        // Cargamos la foto con Coil si existe
                        if (!it.avatar_url.isNullOrEmpty()) {
                            ivFotoPerfil.load(it.avatar_url) {
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.ic_user)
                                error(R.drawable.ic_user)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PerfilFragment", "Error cargando datos: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
