package com.example.projecteventclub.atributos.eventos.consultar_evento

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.projecteventclub.R

class activity_ConEvUsuario : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_consult_event_user1, container, false)

        val btnConEveUsu1 = view.findViewById<ImageButton>(R.id.btnConEveUsu1)
        btnConEveUsu1?.setOnClickListener {
            val fragmentConsult2 = activity_ConEvUsu2()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentConsult2)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = activity_ConEvUsuario()
    }
}