package com.example.projecteventclub.atributos.comida.menu

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.projecteventclub.R

class Activity_comidas : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.frag_comidas, container, false)
        

        val qr1 = view.findViewById<ImageView>(R.id.QR1)
        val qr2 = view.findViewById<ImageView>(R.id.QR2)
        val qr3 = view.findViewById<ImageView>(R.id.QR3)
        val qr4 = view.findViewById<ImageView>(R.id.QR4)
        

        val openCameraListener = View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }
        

        qr1?.setOnClickListener(openCameraListener)
        qr2?.setOnClickListener(openCameraListener)
        qr3?.setOnClickListener(openCameraListener)
        qr4?.setOnClickListener(openCameraListener)
        
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = Activity_comidas()
    }
}