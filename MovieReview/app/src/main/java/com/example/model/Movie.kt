package com.example.model

data class Movie(
    val id: Int = 0,
    val name: String,
    val review: String,
    val stars: Int,
    val genre: Genre
)