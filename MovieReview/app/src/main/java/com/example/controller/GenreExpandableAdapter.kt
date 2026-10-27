package com.example.controller

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.model.Genre
import com.example.model.Movie
import com.example.moviereview.R

class GenreExpandableAdapter(
    private val context: Context,
    private val genres: List<Genre>,
    private val moviesByGenre: Map<Genre, List<Movie>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int = genres.size

    override fun getChildrenCount(groupPosition: Int): Int {
        val genre = genres[groupPosition]
        return moviesByGenre[genre]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Genre = genres[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Movie {
        val genre = genres[groupPosition]
        return moviesByGenre[genre]?.get(childPosition) ?: error("Posição de filme inválida")
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_genre_group, parent, false)
        val genre = getGroup(groupPosition)
        val count = getChildrenCount(groupPosition)

        val vIndicator = view.findViewById<View>(R.id.vGenreColorIndicator)
        val tvGenreName = view.findViewById<TextView>(R.id.tvGroupGenreName)
        val tvCount = view.findViewById<TextView>(R.id.tvGroupCount)
        val ivExpand = view.findViewById<ImageView>(R.id.ivExpandIcon)

        val genreColor = genre.colorHex.toInt()

        tvGenreName.text = genre.displayName
        tvGenreName.setTextColor(genreColor)

        val indicatorBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 8f
            setColor(genreColor)
        }
        vIndicator.background = indicatorBg

        tvCount.text = "$count filme(s)"

        ivExpand.rotation = if (isExpanded) 180f else 0f

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        val movie = getChild(groupPosition, childPosition)

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

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}
