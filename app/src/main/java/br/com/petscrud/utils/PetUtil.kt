package br.com.petscrud.utils

import br.com.petscrud.models.Pet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PetUtil {
    private var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")

    fun convertToJson(pet: Pet) = """
        { 
        "nome": "${pet.nome}",
        "raca": "${pet.raca}",
        "peso": "${pet.peso}",
        "nascimento": "${convertDateToString(pet.nascimento)}"
        }
        """.trimIndent()

    fun convertStringToDate(str: String): LocalDate =
        LocalDate.parse(str, dateFormatter)

    fun convertDateToString(date: LocalDate): String =
        date.format(dateFormatter)

}