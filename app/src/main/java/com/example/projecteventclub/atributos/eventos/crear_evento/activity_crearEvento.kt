package com.example.projecteventclub.atributos.eventos.crear_evento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.projecteventclub.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activity_crearEvento.newInstance] factory method to
 * create an instance of this fragment.
 */
class activity_crearEvento : Fragment() {
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
        val view = inflater.inflate(R.layout.frag_crear_event_adm, container, false)

        // Referencia al EditText de Lugar del Evento (Abrir Google Maps)
        val editTxtLugEveAdm = view.findViewById<EditText>(R.id.EditTxtLugEveAdm)
        editTxtLugEveAdm?.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // Botón Crear Evento (Navegar a Evento Creado)
        val btnCreEveAdm = view.findViewById<ImageButton>(R.id.btnCreEveAdm)
        btnCreEveAdm?.setOnClickListener {
            val fragmentCreado = activity_EvenCreado()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentCreado)
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
         * @return A new instance of fragment activity_crearEvento.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activity_crearEvento().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}