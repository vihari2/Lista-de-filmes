package com.example.data.dao

import android.content.Context
import com.example.data.db.DBHelper
import com.example.model.Movie

class MoviesDAO(private val context: Context) {
    private val dbHelper = DBHelper(context)

    fun getAllMovies() : List<Movie> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
        val movies = mutableListOf<Movie>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val stars = cursor.getInt(cursor.getColumnIndexOrThrow("stars"))
            val genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"))
            movies.add(Movie(id, name, stars, genre))
        }
        cursor.close()
        db.close()
        return movies
    }



}