package com.example.projecteventclub.vista_usuario.main.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.projecteventclub.R
import com.example.projecteventclub.atributos.eventos.consultar_evento.activity_consultarEvento
import com.example.projecteventclub.atributos.eventos.crear_evento.activity_crearEvento

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class activity_adminPrincipal : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.frag_main_adm, container, false)

        // Botón Registrar Asistencia para abrir la camara
        val btnRegistroAsis = view.findViewById<ImageButton>(R.id.BtnRegistroAsis)
        btnRegistroAsis?.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }

        // Botón Agregar Evento redirige a crear evento
        val btnAgregarEve = view.findViewById<ImageButton>(R.id.BtnAgregarEve)
        btnAgregarEve?.setOnClickListener {
            val fragment = activity_crearEvento()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Botón Consultar Evento
        val btnConsultarEve = view.findViewById<ImageButton>(R.id.BtnConsultarEve)
        btnConsultarEve?.setOnClickListener {
            val fragment = activity_consultarEvento()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activity_adminPrincipal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}