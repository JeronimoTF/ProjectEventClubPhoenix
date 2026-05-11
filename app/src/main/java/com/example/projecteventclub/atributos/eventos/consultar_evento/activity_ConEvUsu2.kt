package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.projecteventclub.R

class activity_ConEvUsu2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_consult_event_user2, container, false)

        // 1. Referenciamos los elementos visuales del XML
        val editNombre = view.findViewById<EditText>(R.id.EditTxtNomEveUsu2)
        val editFecHor = view.findViewById<EditText>(R.id.EditTxtFecHorEveUsu2)
        val editDesc = view.findViewById<EditText>(R.id.EditTxtDesEveUsu2)
        val editLocalidad = view.findViewById<EditText>(R.id.EditTxtLocZonUsu2)
        val editPisoSilla = view.findViewById<EditText>(R.id.EditTxtPisoSillaUsu2)
        val btnBack = view.findViewById<ImageView>(R.id.btnBackConEveUsu2)

        // 2. Extraemos los datos de la "maleta" (arguments)
        val nombre = arguments?.getString("nombre")
        val descripcion = arguments?.getString("descripcion")
        val fecha = arguments?.getString("fecha")
        val hora = arguments?.getString("hora")
        val lugar = arguments?.getString("lugar")
        val localidad = arguments?.getString("localidad")
        val pisoSilla = arguments?.getString("pisoSilla")

        // 3. Ponemos los datos en los cuadros de texto
        editNombre?.setText(nombre)
        editFecHor?.setText("$fecha $hora") // Combinamos fecha y hora
        editDesc?.setText(descripcion)
        editLocalidad?.setText(localidad ?: "")
        editPisoSilla?.setText(pisoSilla ?: "")

        // 4. Configuramos el botón de volver
        btnBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
