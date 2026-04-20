package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projecteventclub.R

class activity_consultarEvento : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_consult_event_adm, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = activity_consultarEvento()
    }
}