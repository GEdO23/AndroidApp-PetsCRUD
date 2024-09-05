package br.com.petscrud.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateUtil {
    private var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun toDate(str: String): LocalDate =
        LocalDate.parse(str, dateFormatter)

    fun toString(date: LocalDate): String =
        date.format(dateFormatter)
}