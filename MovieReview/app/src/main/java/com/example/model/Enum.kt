package com.example.model

enum class Genre(
    val displayName: String,
    val color: Int
) {
    ACTION("Ação", 0xFFE57373.toInt()),
    COMEDY("Comédia", 0xFFFFB74D.toInt()),
    DRAMA("Drama", 0xFF9575CD.toInt()),
    HORROR("Terror", 0xFF78909C.toInt()),
    ROMANCE("Romance", 0xFFF06292.toInt()),
    SCI_FI("Ficção científica", 0xFF64B5F6.toInt()),
    ANIMATION("Animação", 0xFF81C784.toInt()),
    DOCUMENTARY("Documentário", 0xFF4DB6AC.toInt())
}