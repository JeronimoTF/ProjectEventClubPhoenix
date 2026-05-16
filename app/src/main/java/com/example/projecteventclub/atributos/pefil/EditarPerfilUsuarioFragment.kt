package com.example.projecteventclub.atributos.pefil

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.models.UserProfile
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class EditarPerfilUsuarioFragment : Fragment() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var etNombres: TextInputEditText
    private lateinit var etApellidos: TextInputEditText
    private lateinit var etEdad: TextInputEditText
    private lateinit var spGenero: MaterialAutoCompleteTextView
    private lateinit var spTipoDocumento: MaterialAutoCompleteTextView
    private lateinit var etDocumento: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var etCelular: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etCiudad: TextInputEditText
    private lateinit var etLocalidad: TextInputEditText
    private lateinit var etBarrio: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var etContra: TextInputEditText
    private lateinit var btnGuardar: Button

    private var selectedImageUri: Uri? = null
    private var currentProfile: UserProfile? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivFotoPerfil.load(it) { transformations(CircleCropTransformation()) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_editar_perfil_usuario, container, false)
        initViews(view)
        loadUserData()
        return view
    }

    private fun initViews(view: View) {
        ivFotoPerfil = view.findViewById(R.id.iv_editar_foto)
        etNombres = view.findViewById(R.id.etNombres)
        etApellidos = view.findViewById(R.id.etApellidos)
        etEdad = view.findViewById(R.id.etEdad)
        spGenero = view.findViewById(R.id.spGenero)
        spTipoDocumento = view.findViewById(R.id.spTipoDocumento)
        etDocumento = view.findViewById(R.id.etDocumento)
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        etCelular = view.findViewById(R.id.etCelular)
        etCorreo = view.findViewById(R.id.etCorreo)
        etCiudad = view.findViewById(R.id.etCiudadResi)
        etLocalidad = view.findViewById(R.id.etLocalidad)
        etBarrio = view.findViewById(R.id.etBarrio)
        etDireccion = view.findViewById(R.id.etDireccion)
        etContra = view.findViewById(R.id.etContra)
        btnGuardar = view.findViewById(R.id.btnGuardarCambios)

        view.findViewById<View>(R.id.fab_cambiar_foto)?.setOnClickListener { imagePickerLauncher.launch("image/*") }
        etFechaNacimiento.setOnClickListener { showDatePicker() }
        btnGuardar.setOnClickListener { if (validateInputs()) updateProfile() }
    }

    private fun loadUserData() {
        val user = SupaBaseClient.client.auth.currentUserOrNull() ?: return
        val userId = user.id

        lifecycleScope.launch {
            try {
                val profile = withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["perfiles"]
                        .select { filter { eq("id", userId) } }
                        .decodeSingleOrNull<UserProfile>()
                }
                
                if (profile != null) {
                    currentProfile = profile
                    withContext(Dispatchers.Main) {
                        etNombres.setText(profile.nombres ?: "")
                        etApellidos.setText(profile.apellidos ?: "")
                        etEdad.setText(profile.edad ?: "")
                        spGenero.setText(profile.genero ?: "", false)
                        spTipoDocumento.setText(profile.tipo_documento ?: "", false)
                        etDocumento.setText(profile.documento ?: "")
                        etFechaNacimiento.setText(profile.fecha_nacimiento ?: "")
                        etCelular.setText(profile.celular ?: "")
                        etCorreo.setText(profile.correo ?: user.email ?: "")
                        etCiudad.setText(profile.ciudad ?: "")
                        etLocalidad.setText(profile.localidad ?: "")
                        etBarrio.setText(profile.barrio ?: "")
                        etDireccion.setText(profile.direccion ?: "")
                        
                        if (!profile.avatar_url.isNullOrEmpty()) {
                            ivFotoPerfil.load(profile.avatar_url) {
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.ic_user)
                                error(R.drawable.ic_user)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("EditarPerfil", "Error al cargar: ${e.message}")
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (etNombres.text.isNullOrBlank()) { etNombres.error = "Requerido"; return false }
        return true
    }

    private fun updateProfile() {
        val userId = SupaBaseClient.client.auth.currentUserOrNull()?.id ?: return
        
        lifecycleScope.launch {
            try {
                var avatarUrl = currentProfile?.avatar_url
                
                selectedImageUri?.let { uri ->
                    val bytes = withContext(Dispatchers.IO) {
                        requireContext().contentResolver.openInputStream(uri)?.readBytes()
                    }
                    if (bytes != null) {
                        val fileName = "$userId/avatar.jpg"
                        withContext(Dispatchers.IO) {
                            SupaBaseClient.client.storage.from("fotos_perfil").upload(fileName, bytes) {
                                upsert = true
                            }
                        }
                        avatarUrl = SupaBaseClient.client.storage.from("fotos_perfil").publicUrl(fileName)
                    }
                }

                val updatedProfile = UserProfile(
                    id = userId,
                    nombres = etNombres.text.toString().trim(),
                    apellidos = etApellidos.text.toString().trim(),
                    edad = etEdad.text.toString().trim(),
                    genero = spGenero.text.toString().trim(),
                    tipo_documento = spTipoDocumento.text.toString().trim(),
                    documento = etDocumento.text.toString().trim(),
                    fecha_nacimiento = etFechaNacimiento.text.toString().trim(),
                    celular = etCelular.text.toString().trim(),
                    correo = etCorreo.text.toString().trim(),
                    ciudad = etCiudad.text.toString().trim(),
                    localidad = etLocalidad.text.toString().trim(),
                    barrio = etBarrio.text.toString().trim(),
                    direccion = etDireccion.text.toString().trim(),
                    avatar_url = avatarUrl
                )

                withContext(Dispatchers.IO) {
                    SupaBaseClient.client.postgrest["perfiles"].upsert(updatedProfile)
                }

                withContext(Dispatchers.Main) {
                    (activity as? MainActivity)?.actualizarInterfaz()
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)
            etFechaNacimiento.setText(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
