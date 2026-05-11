package com.example.projecteventclub.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val nombres: String? = "",
    val apellidos: String? = "",
    val edad: String? = "",
    val genero: String? = "",
    val tipo_documento: String? = "",
    val documento: String? = "",
    val fecha_nacimiento: String? = "",
    val celular: String? = "",
    val correo: String? = "",
    val ciudad: String? = "",
    val localidad: String? = "",
    val barrio: String? = "",
    val direccion: String? = "",
    val avatar_url: String? = null,
    val rol: String? = "USER" // Valores posibles: "ADMIN", "USER"
)
