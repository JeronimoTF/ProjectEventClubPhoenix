package com.example.projecteventclub.models

import kotlinx.serialization.Serializable

@Serializable
data class Evento(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val hora: String,
    val lugar: String,
    val localidad: String? = "",
    val pisoSilla: String? = ""
)
