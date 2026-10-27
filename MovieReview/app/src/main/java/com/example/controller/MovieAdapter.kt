package com.example.controller

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.model.Movie
import com.example.moviereview.R

class MovieAdapter(
    context: Context,
    private val movies: List<Movie>
) : ArrayAdapter<Movie>(context, 0, movies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        val movie = movies[position]

        val tvTitle = view.findViewById<TextView>(R.id.tvMovieTitle)
        val tvGenre = view.findViewById<TextView>(R.id.tvMovieGenre)
        val tvStars = view.findViewById<TextView>(R.id.tvMovieStars)
        val tvReview = view.findViewById<TextView>(R.id.tvMovieReview)

        tvTitle.text = movie.name
        tvGenre.text = movie.genre.displayName

        val badgeBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 24f
            setColor(movie.genre.colorHex.toInt())
        }
        tvGenre.background = badgeBackground

        val fullStars = "★".repeat(movie.stars)
        val emptyStars = "☆".repeat(5 - movie.stars)
        tvStars.text = "$fullStars$emptyStars"

        tvReview.text = movie.review.ifEmpty { "Sem resenha cadastrada." }

        return view
    }
}
