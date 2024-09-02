package br.com.petscrud.models

import java.time.LocalDate

data class Pet(
    val nome: String,
    val raca: String,
    val peso: Float,
    val nascimento: LocalDate
)
