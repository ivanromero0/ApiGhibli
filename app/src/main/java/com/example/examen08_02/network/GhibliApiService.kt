package com.example.examen08_02.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

private const val BASE_URL = "https://ghibliapi.vercel.app"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL).build()

interface GhibliApiService {
    @GET("films")
    suspend fun getFilms():List<GhibliFilm>
    
    @GET("people")
    suspend fun getPeople():List<GhibliPeople>
}

object GhibliFilms {
    val retrofitService : GhibliApiService by lazy {
        retrofit.create(GhibliApiService::class.java)
    }
}
