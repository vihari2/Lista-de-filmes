package com.example.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.model.Genre
import com.example.moviereview.R

data class GenreGroupItem(val genre: Genre, val count: Int)

class GroupAdapter(
    context: Context,
    private val groups: List<GenreGroupItem>
) : ArrayAdapter<GenreGroupItem>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_genre_group, parent, false)
        val item = groups[position]

        val tvGenreName = view.findViewById<TextView>(R.id.tvGroupGenreName)
        val tvCount = view.findViewById<TextView>(R.id.tvGroupCount)

        tvGenreName.text = item.genre.displayName
        tvCount.text = "${item.count} filme(s)"

        return view
    }
}
