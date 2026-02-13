package com.example.examen08_02.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.examen08_02.R
import com.example.examen08_02.network.GhibliFilm
import com.example.examen08_02.network.GhibliPeople
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(viewModel: HomeViewModel, auth: FirebaseAuth, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = { HomeTopBar(viewModel, auth) },
        bottomBar = { BottomNavigationButtons(viewModel) },
        content = {
            when (viewModel.currentView) {
                ContentView.FILMS -> {
                    when (val ghibliFilmUiState = viewModel.ghibliFilmUiState) {
                        is GhibliFilmUiState.Success -> FilmsSuccessContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it),
                            ghibliFilmUiState.films
                        )
                        is GhibliFilmUiState.Error -> ErrorContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it),
                            message = "Error cargando películas"
                        )
                        is GhibliFilmUiState.Loading -> LoadingContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }
                }
                ContentView.PEOPLE -> {
                    when (val ghibliPeopleUiState = viewModel.ghibliPeopleUiState) {
                        is GhibliPeopleUiState.Success -> PeopleSuccessContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it),
                            ghibliPeopleUiState.people
                        )
                        is GhibliPeopleUiState.Error -> ErrorContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it),
                            message = "Error cargando personajes"
                        )
                        is GhibliPeopleUiState.Loading -> LoadingContent(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(it)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationButtons(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { viewModel.getFilms() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.Movie, contentDescription = "Películas")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Películas")
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Button(
            onClick = { viewModel.getPeople() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = Icons.Default.People, contentDescription = "Personajes")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Personajes")
        }
    }
}

@Composable
fun ErrorContent(modifier: Modifier, message: String) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "Connection error"
        )
        Text(text = message,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun LoadingContent(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
    }
}

@Composable
fun FilmsSuccessContent(modifier: Modifier, films: List<GhibliFilm>) {
    LazyColumn (modifier = modifier){
        item() {
            Text(
                "Total: ${films.size} películas",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        items(films.size) {GhibliFilmCard(films[it])}
    }
}

@Composable
fun PeopleSuccessContent(modifier: Modifier, people: List<GhibliPeople>) {
    LazyColumn (modifier = modifier){
        item() {
            Text(
                "Total: ${people.size} personajes",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        items(people.size) {GhibliPeopleCard(people[it])}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(viewModel: HomeViewModel, auth: FirebaseAuth) {
    TopAppBar(
        title = { 
            Text(text = when(viewModel.currentView) {
                ContentView.FILMS -> "Películas de ${auth.currentUser?.email?.split('@')?.get(0)}"
                ContentView.PEOPLE -> "Personajes de ${auth.currentUser?.email?.split('@')?.get(0)}"
            })
        },
        actions = {
            IconButton(onClick = { 
                when(viewModel.currentView) {
                    ContentView.FILMS -> viewModel.getFilms()
                    ContentView.PEOPLE -> viewModel.getPeople()
                }
            },
                content = {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                })
        })
}

@Composable
fun GhibliFilmCard(film: GhibliFilm) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text(film.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(film.originalTitle,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data(film.image)
                        .crossfade(true)
                        .build(),
                        contentDescription = "Film photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun GhibliPeopleCard(person: GhibliPeople) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Text(
                text = person.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Género", person.gender)
                    InfoRow("Edad", person.age)
                }
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Color de ojos", person.eyeColor)
                    InfoRow("Color de pelo", person.hairColor)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp
        )
    }
}