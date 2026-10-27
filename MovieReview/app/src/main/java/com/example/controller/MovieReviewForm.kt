package com.example.controller

import android.os.Bundle
import android.view.View
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
    private var movieId: Int? = null

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
        val btnDeleteMovie = findViewById<Button>(R.id.btnDeleteMovie) // Você precisará adicionar este botão no XML

        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Genre.entries.map { it.displayName }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGenre.adapter = genreAdapter

        // Verifica se é uma edição
        if (intent.hasExtra("MOVIE_ID")) {
            movieId = intent.getIntExtra("MOVIE_ID", -1)
            etMovieName.setText(intent.getStringExtra("MOVIE_NAME"))
            etMovieReview.setText(intent.getStringExtra("MOVIE_REVIEW"))
            ratingMovie.rating = intent.getIntExtra("MOVIE_STARS", 0).toFloat()
            
            val genreString = intent.getStringExtra("MOVIE_GENRE")
            if (genreString != null) {
                val genreIndex = Genre.valueOf(genreString).ordinal
                spGenre.setSelection(genreIndex)
            }
            
            btnDeleteMovie.visibility = View.VISIBLE
        } else {
            btnDeleteMovie.visibility = View.GONE
        }

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
                    id = movieId ?: 0,
                    name = name,
                    review = review,
                    stars = stars,
                    genre = selectedGenre
                )
                
                val success = if (movieId != null) {
                    moviesDAO.updateMovie(movie) > 0
                } else {
                    moviesDAO.insertMovie(movie) != -1L
                }

                if (success) {
                    Toast.makeText(
                        this,
                        if (movieId != null) "Filme atualizado com sucesso!" else "Filme salvo com sucesso!",
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

        btnDeleteMovie.setOnClickListener {
            movieId?.let { id ->
                val result = moviesDAO.deleteMovie(id)
                if (result > 0) {
                    Toast.makeText(this, "Filme excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao excluir filme.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancelMovie.setOnClickListener {
            finish()
        }
    }
}