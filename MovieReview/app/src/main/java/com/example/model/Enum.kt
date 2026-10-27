package com.example.model

enum class Genre(
    val displayName: String,
    val colorHex: Long
) {
    ACTION("Ação", 0xFFEF4444),
    COMEDY("Comédia", 0xFFF59E0B),
    DRAMA("Drama", 0xFF8B5CF6),
    HORROR("Terror", 0xFF475569),
    ROMANCE("Romance", 0xFFEC4899),
    SCI_FI("Ficção científica", 0xFF3B82F6),
    ANIMATION("Animação", 0xFF10B981),
    DOCUMENTARY("Documentário", 0xFF14B8A6)
}
