package com.example.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.data.dao.MoviesDAO
import com.example.model.Genre
import com.example.model.Movie
import com.example.moviereview.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch

class MainActivity : AppCompatActivity() {

    private lateinit var listMovies: ListView
    private lateinit var expandableListMovies: ExpandableListView
    private lateinit var spFilterGenre: Spinner
    private lateinit var spFilterStars: Spinner
    private lateinit var swGroupByGenre: MaterialSwitch
    private lateinit var btnAddMovie: ExtendedFloatingActionButton
    private lateinit var moviesDAO: MoviesDAO

    private var moviesList: List<Movie> = listOf()
    private var filteredList: List<Movie> = listOf()
    private var currentGroupedGenres: List<Genre> = listOf()
    private var isGroupedMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val density = resources.displayMetrics.density
            val paddingPx = (16 * density).toInt()
            v.setPadding(
                systemBars.left + paddingPx,
                systemBars.top + paddingPx,
                systemBars.right + paddingPx,
                systemBars.bottom
            )
            insets
        }

        moviesDAO = MoviesDAO(this)

        btnAddMovie = findViewById(R.id.btnAddMovie)
        listMovies = findViewById(R.id.listMovies)
        expandableListMovies = findViewById(R.id.expandableListMovies)
        spFilterGenre = findViewById(R.id.spFilterGenre)
        spFilterStars = findViewById(R.id.spFilterStars)
        swGroupByGenre = findViewById(R.id.swGroupByGenre)

        setupFilters()

        btnAddMovie.setOnClickListener {
            val intent = Intent(this, MovieReviewForm::class.java)
            startActivity(intent)
        }

        listMovies.setOnItemClickListener { _, _, position, _ ->
            if (position in filteredList.indices) {
                val selectedMovie = filteredList[position]
                openMovieForm(selectedMovie)
            }
        }

        expandableListMovies.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val genre = currentGroupedGenres.getOrNull(groupPosition)
            if (genre != null) {
                val moviesInGenre = moviesList.filter { it.genre == genre }
                if (childPosition in moviesInGenre.indices) {
                    val selectedMovie = moviesInGenre[childPosition]
                    openMovieForm(selectedMovie)
                    return@setOnChildClickListener true
                }
            }
            false
        }
    }

    private fun openMovieForm(movie: Movie) {
        val intent = Intent(this, MovieReviewForm::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
            putExtra("MOVIE_NAME", movie.name)
            putExtra("MOVIE_REVIEW", movie.review)
            putExtra("MOVIE_STARS", movie.stars)
            putExtra("MOVIE_GENRE", movie.genre.name)
        }
        startActivity(intent)
    }

    private fun setupFilters() {
        val genreOptions = mutableListOf("Todos os gêneros")
        genreOptions.addAll(Genre.entries.map { it.displayName })
        val genreAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genreOptions)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFilterGenre.adapter = genreAdapter

        val starsOptions = listOf(
            "Todas as estrelas",
            "1+ ★",
            "2+ ★",
            "3+ ★",
            "4+ ★",
            "5 ★"
        )
        val starsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, starsOptions)
        starsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFilterStars.adapter = starsAdapter

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFiltersAndRender()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spFilterGenre.onItemSelectedListener = listener
        spFilterStars.onItemSelectedListener = listener

        swGroupByGenre.setOnCheckedChangeListener { _, isChecked ->
            isGroupedMode = isChecked
            spFilterGenre.isEnabled = !isChecked
            spFilterStars.isEnabled = !isChecked

            if (isChecked) {
                listMovies.visibility = View.GONE
                expandableListMovies.visibility = View.VISIBLE
            } else {
                listMovies.visibility = View.VISIBLE
                expandableListMovies.visibility = View.GONE
            }

            applyFiltersAndRender()
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun loadMovies() {
        moviesList = moviesDAO.getAllMovies()
        applyFiltersAndRender()
    }

    private fun applyFiltersAndRender() {
        if (isGroupedMode) {
            currentGroupedGenres = Genre.entries.sortedWith(
                compareByDescending<Genre> { genre -> moviesList.count { it.genre == genre } }
                    .thenBy { it.ordinal }
            )

            val moviesByGenre = currentGroupedGenres.associateWith { genre ->
                moviesList.filter { it.genre == genre }
            }

            val expandableAdapter = GenreExpandableAdapter(this, currentGroupedGenres, moviesByGenre)
            expandableListMovies.setAdapter(expandableAdapter)

            for (i in currentGroupedGenres.indices) {
                if (moviesByGenre[currentGroupedGenres[i]]?.isNotEmpty() == true) {
                    expandableListMovies.expandGroup(i)
                }
            }
        } else {
            val selectedGenrePos = spFilterGenre.selectedItemPosition
            val selectedStarPos = spFilterStars.selectedItemPosition

            val minStars = if (selectedStarPos > 0) selectedStarPos else 0

            filteredList = moviesList.filter { movie ->
                val matchesGenre = if (selectedGenrePos > 0) {
                    val genre = Genre.entries[selectedGenrePos - 1]
                    movie.genre == genre
                } else {
                    true
                }

                val matchesStars = movie.stars >= minStars

                matchesGenre && matchesStars
            }

            val adapter = MovieAdapter(this, filteredList)
            listMovies.adapter = adapter
        }
    }
}
