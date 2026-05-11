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
import com.example.projecteventclub.atributos.anfitrion.FragmentPedidosPendientes
import com.example.projecteventclub.atributos.anfitrion.FragmentPedidosEntregados

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activity_adminPrincipal.newInstance] factory method to
 * create an instance of this fragment.
 */
class activity_adminPrincipal : Fragment() {
    // TODO: Rename and change types of parameters
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

        // Botón Localizar Evento abre el google maps
        val btnLocalizarEve = view.findViewById<ImageButton>(R.id.BtnlocalizarEve)
        btnLocalizarEve?.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=4.6097,-74.0817")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
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

        // Botón Editar Evento
        val btnEditarEve = view.findViewById<ImageButton>(R.id.BtnEditarEve)
        btnEditarEve?.setOnClickListener {
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

        val btnPendientes = view.findViewById<ImageButton>(R.id.BtnPedidosPendientes)
        btnPendientes?.setOnClickListener {
            val fragment = FragmentPedidosPendientes()  // ← CORRECTO
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        val btnEntregados = view.findViewById<ImageButton>(R.id.BtnPedidosEntregados)
        btnEntregados?.setOnClickListener {
            val fragment = FragmentPedidosEntregados()  // ← CORRECTO
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment activity_adminPrincipal.
         */
        // TODO: Rename and change types and number of parameters
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