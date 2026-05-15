package com.example.projecteventclub.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
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
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class Login : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var ivHuella: ImageView
    private lateinit var tvHuella: TextView

    // ✅ GOOGLE
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_GOOGLE_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        ivHuella = findViewById(R.id.ivHuella)
        tvHuella = findViewById(R.id.tvHuella)

        configurarBiometria()
        configurarVisibilidadHuella()

        // ✅ CONFIGURACIÓN GOOGLE (igual Login 1)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("195889917013-vfjdc707d6lu5qauhv50qkrbco945b8a.apps.googleusercontent.com") // ✅ tu client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // ✅ LOGIN MANUAL
        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
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

                    guardarCredenciales(email, password)

                    irAPantallaPrincipal()

                } catch (e: Exception) {
                    Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // ✅ GOOGLE LOGIN
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                startActivityForResult(
                    googleSignInClient.signInIntent,
                    RC_GOOGLE_SIGN_IN
                )
            }
        }

        // ✅ BIOMETRÍA CLICK
        val biometricListener = {
            if (existenCredencialesGuardadas()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Toast.makeText(this, "Primero inicia sesión manualmente", Toast.LENGTH_LONG).show()
            }
        }

        ivHuella.setOnClickListener { biometricListener() }
        tvHuella.setOnClickListener { biometricListener() }

        // ✅ OTROS
        findViewById<TextView>(R.id.linkRegistro).setOnClickListener {
            startActivity(Intent(this, RegistroDatos::class.java))
        }

        findViewById<TextView>(R.id.RecCambioContra).setOnClickListener {
            startActivity(Intent(this, RecuperarContrasena::class.java))
        }
    }

    // ✅ RESULTADO GOOGLE (igual Login 1)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                val idToken = account.idToken

                // ✅ DEBUG 1: Ver si llega token
                Log.d("GOOGLE_DEBUG", "ID TOKEN: $idToken")

                if (idToken == null) {
                    Toast.makeText(this, "Error: token nulo", Toast.LENGTH_LONG).show()
                    return
                }

                lifecycleScope.launch {
                    try {
                        SupaBaseClient.client.auth.signInWith(IDToken) {
                            this.idToken = idToken
                            provider = Google
                        }

                        // ✅ DEBUG 2: confirmar login
                        Log.d("GOOGLE_DEBUG", "LOGIN EXITOSO")

                        irAPantallaPrincipal()

                    } catch (e: Exception) {
                        // ✅ DEBUG 3: VER ERROR REAL
                        Log.e("GOOGLE_ERROR", "Supabase error: ${e.message}")

                        Toast.makeText(
                            this@Login,
                            "Error real: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("GOOGLE_ERROR", "Google error", e)

                Toast.makeText(
                    this,
                    "Error Google: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    // ✅ BIOMETRÍA
    private fun configurarBiometria() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    loginConHuella()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso con huella")
            .setSubtitle("Usa tu huella para ingresar")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun configurarVisibilidadHuella() {
        val biometricManager = BiometricManager.from(this)

        val disponible =
            biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            ) == BiometricManager.BIOMETRIC_SUCCESS

        tvHuella.visibility =
            if (existenCredencialesGuardadas() && disponible) View.VISIBLE else View.GONE

        ivHuella.visibility = tvHuella.visibility
    }

    private fun loginConHuella() {
        val prefs = getSecurePrefs()

        val email = prefs.getString("email", null)
        val pass = prefs.getString("pass", null)

        if (email != null && pass != null) {
            lifecycleScope.launch {
                try {
                    SupaBaseClient.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = pass
                    }

                    irAPantallaPrincipal()

                } catch (e: Exception) {
                    Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ✅ CREDENCIALES SEGURAS
    private fun guardarCredenciales(email: String, pass: String) {
        getSecurePrefs().edit().apply {
            putString("email", email)
            putString("pass", pass)
            apply()
        }
    }

    private fun existenCredencialesGuardadas(): Boolean {
        val prefs = getSecurePrefs()
        return prefs.contains("email") && prefs.contains("pass")
    }

    private fun getSecurePrefs() =
        EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    // ✅ NAVEGACIÓN
    private fun irAPantallaPrincipal() {
        startActivity(Intent(this@Login, MainActivity::class.java))
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        configurarVisibilidadHuella()
    }
}