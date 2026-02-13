package com.example.examen08_02.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GhibliPeople(
    val id: String,
    val name: String,
    val gender: String,
    val age: String,
    @SerialName(value = "eye_color")
    val eyeColor: String,
    @SerialName(value = "hair_color")
    val hairColor: String
)
