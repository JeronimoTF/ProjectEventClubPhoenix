package com.example.projecteventclub.vista_usuario.main.usuarios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.projecteventclub.R
import com.example.projecteventclub.atributos.comida.menu.Activity_comidas
import com.example.projecteventclub.atributos.eventos.consultar_evento.activity_ConEvUsuario

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activity_usuarioPrincipal.newInstance] factory method to
 * create an instance of this fragment.
 */
class activity_usuarioPrincipal : Fragment() {

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_main_user, container, false)

        // Botón Localizar Evento
        val btnLocEveMuser = view.findViewById<ImageButton>(R.id.btnLocEveMuser)
        btnLocEveMuser?.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=4.6097,-74.0817")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // Botón Pedir Comida
        val btnPedComMuser = view.findViewById<ImageButton>(R.id.btnPedComMuser)
        btnPedComMuser?.setOnClickListener {
            val fragmentComidas = Activity_comidas()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentComidas)
                .addToBackStack(null)
                .commit()
        }

        // Botón Consultar Evento
        val btnConEveMuser = view.findViewById<ImageButton>(R.id.btnConEveMuser)
        btnConEveMuser?.setOnClickListener {
            val fragmentConsult = activity_ConEvUsuario()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentConsult)
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
         * @return A new instance of fragment activity_usuarioPrincipal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activity_usuarioPrincipal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}