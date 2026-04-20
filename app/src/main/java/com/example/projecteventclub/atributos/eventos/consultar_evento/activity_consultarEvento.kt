package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.projecteventclub.R

class activity_consultarEvento : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_consult_event_adm, container, false)

        // Botón Localizar Evento (Abre Google Maps en Bogotá)
        // El ID btnEliEveAdm corresponde al icono de localización en el layout frag_consult_event_adm.xml
        val btnLocalizar = view.findViewById<ImageButton>(R.id.btnEliEveAdm)
        btnLocalizar?.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:4.6097,-74.0817?q=4.6097,-74.0817(Bogotá)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = activity_consultarEvento()
    }
}