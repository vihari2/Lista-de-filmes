package com.example.controller

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.data.dao.MoviesDAO
import com.example.model.Genre
import com.example.model.Movie
import com.example.moviereview.R

class MovieReviewForm : AppCompatActivity() {

    private lateinit var moviesDAO: MoviesDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_review_form)

        moviesDAO = MoviesDAO(this)

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

        val etMovieName = findViewById<EditText>(R.id.etMovieName)
        val spGenre = findViewById<Spinner>(R.id.spGenre)
        val ratingMovie = findViewById<RatingBar>(R.id.ratingMovie)
        val etMovieReview = findViewById<EditText>(R.id.etMovieReview)

        val btnSaveMovie = findViewById<Button>(R.id.btnSaveMovie)
        val btnCancelMovie = findViewById<Button>(R.id.btnCancelMovie)

        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Genre.entries.map { it.displayName }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGenre.adapter = genreAdapter

        btnSaveMovie.setOnClickListener {
            val name = etMovieName.text.toString().trim()
            val review = etMovieReview.text.toString().trim()
            val stars = ratingMovie.rating.toInt()
            val selectedGenre = Genre.entries[spGenre.selectedItemPosition]

            if (name.isEmpty() || review.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha o nome e a resenha do filme.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val movie = Movie(
                    name = name,
                    review = review,
                    stars = stars,
                    genre = selectedGenre
                )
                val result = moviesDAO.insertMovie(movie)
                if (result != -1L) {
                    Toast.makeText(
                        this,
                        "Filme salvo com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Erro ao salvar o filme.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnCancelMovie.setOnClickListener {
            finish()
        }
    }
}