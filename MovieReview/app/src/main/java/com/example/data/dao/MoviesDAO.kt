package com.example.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.data.db.DBHelper
import com.example.model.Genre
import com.example.model.Movie

class MoviesDAO(private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun getAllMovies(): List<Movie> {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            DBHelper.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val movies = mutableListOf<Movie>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val review = cursor.getString(cursor.getColumnIndexOrThrow("review"))
            val stars = cursor.getInt(cursor.getColumnIndexOrThrow("stars"))

            val genreName = cursor.getString(
                cursor.getColumnIndexOrThrow("genre")
            )

            val genre = Genre.valueOf(genreName)

            val movie = Movie(
                id = id,
                name = name,
                review = review,
                stars = stars,
                genre = genre
            )

            movies.add(movie)
        }

        cursor.close()
        db.close()

        return movies
    }

    fun insertMovie(movie: Movie): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", movie.name)
            put("review", movie.review)
            put("stars", movie.stars)
            put("genre", movie.genre.name)
        }

        val result = db.insert(DBHelper.TABLE_NAME, null, values)

        db.close()

        return result
    }

    fun updateMovie(movie: Movie): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", movie.name)
            put("review", movie.review)
            put("stars", movie.stars)
            put("genre", movie.genre.name)
        }

        val result = db.update(
            DBHelper.TABLE_NAME,
            values,
            "id = ?",
            arrayOf(movie.id.toString())
        )

        db.close()

        return result
    }

    fun deleteMovie(id: Int): Int {
        val db = dbHelper.writableDatabase

        val result = db.delete(
            DBHelper.TABLE_NAME,
            "id = ?",
            arrayOf(id.toString())
        )

        db.close()

        return result
    }
}