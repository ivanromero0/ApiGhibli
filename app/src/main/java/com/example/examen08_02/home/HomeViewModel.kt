package com.example.examen08_02.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examen08_02.network.GhibliApiService
import com.example.examen08_02.network.GhibliFilm
import com.example.examen08_02.network.GhibliPeople
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class HomeViewModel (private val apiService: GhibliApiService): ViewModel() {
    var ghibliFilmUiState: GhibliFilmUiState by mutableStateOf(GhibliFilmUiState.Loading)
    var ghibliPeopleUiState: GhibliPeopleUiState by mutableStateOf(GhibliPeopleUiState.Loading)
    var currentView: ContentView by mutableStateOf(ContentView.FILMS)

    init { getFilms() }

    fun getFilms() {
        currentView = ContentView.FILMS
        ghibliFilmUiState = GhibliFilmUiState.Loading
        viewModelScope.launch {
            ghibliFilmUiState = try {
                GhibliFilmUiState.Success(apiService.getFilms())
            } catch (ex: IOException) {
                Log.e("GHIBLI_API", "Error fetching films: ${ex.message}", ex)
                GhibliFilmUiState.Error
            } catch (ex: Exception) {
                Log.e("GHIBLI_API", "Unexpected error: ${ex.message}", ex)
                GhibliFilmUiState.Error
            }
        }
    }
    
    fun getPeople() {
        currentView = ContentView.PEOPLE
        ghibliPeopleUiState = GhibliPeopleUiState.Loading
        viewModelScope.launch {
            ghibliPeopleUiState = try {
                GhibliPeopleUiState.Success(apiService.getPeople())
            } catch (ex: IOException) {
                Log.e("GHIBLI_API", "Error fetching people: ${ex.message}", ex)
                GhibliPeopleUiState.Error
            } catch (ex: Exception) {
                Log.e("GHIBLI_API", "Unexpected error: ${ex.message}", ex)
                GhibliPeopleUiState.Error
            }
        }
    }
}

enum class ContentView {
    FILMS,
    PEOPLE
}

sealed interface GhibliFilmUiState {
    data class Success(val films: List<GhibliFilm>) : GhibliFilmUiState
    object Error : GhibliFilmUiState
    object Loading : GhibliFilmUiState
}

sealed interface GhibliPeopleUiState {
    data class Success(val people: List<GhibliPeople>) : GhibliPeopleUiState
    object Error : GhibliPeopleUiState
    object Loading : GhibliPeopleUiState
}