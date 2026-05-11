package com.example.projecteventclub.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.projecteventclub.MainActivity
import com.example.projecteventclub.R
import com.example.projecteventclub.SupaBaseClient
import com.example.projecteventclub.auth.rec_contrasena.RecuperarContrasena
import com.example.projecteventclub.auth.registro.RegistroDatos
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class Login : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val ivHuella = findViewById<ImageView>(R.id.ivHuella)
        val tvHuella = findViewById<TextView>(R.id.tvHuella)

        // Configuración de Biometría
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Si la huella es correcta, intentamos login con datos guardados
                loginConHuella()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Error de huella: $errString", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de sesión biométrico")
            .setSubtitle("Usa tu huella para ingresar")
            .setNegativeButtonText("Usar contraseña")
            .build()

        // Botón ingresar manual
        val btn2: Button = findViewById(R.id.btnIngresar)
        btn2.setOnClickListener {
            val email = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    SupaBaseClient.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    
                    // GUARDAR CREDENCIALES para la próxima vez usar huella
                    guardarCredenciales(email, password)

                    val intent = Intent(this@Login, MainActivity::class.java)
                    intent.putExtra("USER_TYPE", "USER")
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Click en la imagen o texto de la huella
        val biometricListener = {
            if (existenCredencialesGuardadas()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Toast.makeText(this, "Primero debes iniciar sesión manualmente una vez", Toast.LENGTH_LONG).show()
            }
        }
        ivHuella.setOnClickListener { biometricListener() }
        tvHuella.setOnClickListener { biometricListener() }

        // Resto de botones
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<TextView>(R.id.linkRegistro).setOnClickListener {
            startActivity(Intent(this, RegistroDatos::class.java))
        }
        findViewById<TextView>(R.id.RecCambioContra).setOnClickListener {
            startActivity(Intent(this, RecuperarContrasena::class.java))
        }
    }

    private fun guardarCredenciales(email: String, pass: String) {
        val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            this, "secret_shared_prefs", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("pass", pass)
            apply()
        }
    }

    private fun existenCredencialesGuardadas(): Boolean {
        val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            this, "secret_shared_prefs", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.contains("email") && sharedPreferences.contains("pass")
    }

    private fun loginConHuella() {
        val masterKey = MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            this, "secret_shared_prefs", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val email = sharedPreferences.getString("email", null)
        val pass = sharedPreferences.getString("pass", null)

        if (email != null && pass != null) {
            lifecycleScope.launch {
                try {
                    SupaBaseClient.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = pass
                    }
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@Login, "Error al entrar con huella: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
