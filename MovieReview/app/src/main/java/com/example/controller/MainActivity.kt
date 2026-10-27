package com.example.controller

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.data.dao.MoviesDAO
import com.example.moviereview.R

class MainActivity : AppCompatActivity() {

    private lateinit var listMovies: ListView
    private lateinit var moviesDAO: MoviesDAO
    private var moviesList: List<com.example.model.Movie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val density = resources.displayMetrics.density
            val paddingPx = (20 * density).toInt()
            v.setPadding(
                systemBars.left + paddingPx,
                systemBars.top + paddingPx,
                systemBars.right + paddingPx,
                systemBars.bottom + paddingPx
            )
            insets
        }

        moviesDAO = MoviesDAO(this)

        val btnAddMovie = findViewById<Button>(R.id.btnAddMovie)
        listMovies = findViewById<ListView>(R.id.listMovies)

        btnAddMovie.setOnClickListener {
            val intent = Intent(this, MovieReviewForm::class.java)
            startActivity(intent)
        }

        listMovies.setOnItemClickListener { _, _, position, _ ->
            val selectedMovie = moviesList[position]
            val intent = Intent(this, MovieReviewForm::class.java).apply {
                putExtra("MOVIE_ID", selectedMovie.id)
                putExtra("MOVIE_NAME", selectedMovie.name)
                putExtra("MOVIE_REVIEW", selectedMovie.review)
                putExtra("MOVIE_STARS", selectedMovie.stars)
                putExtra("MOVIE_GENRE", selectedMovie.genre.name)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun loadMovies() {
        moviesList = moviesDAO.getAllMovies()
        val movieTitles = moviesList.map { "${it.name} (${it.genre.displayName}) - ${it.stars}★" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movieTitles)
        listMovies.adapter = adapter
    }
}