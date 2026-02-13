package com.example.examen08_02.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GhibliFilm(
    val id: String,
    val title: String,
    @SerialName(value = "original_title")
    val originalTitle: String,
    val image: String
)