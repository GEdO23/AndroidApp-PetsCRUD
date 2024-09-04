package br.com.petscrud.utils

import android.widget.EditText
import br.com.petscrud.models.Pet
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PetUtil {
    private var numberFormat = NumberFormat.getInstance(Locale.ROOT)
    private var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")

    fun toJson(pet: Pet) = """
        { 
        "nome": "${pet.nome}",
        "raca": "${pet.raca}",
        "peso": "${pet.peso}",
        "nascimento": "${pet.nascimento}"
        }
        """.trimIndent()

    fun toDate(edt: EditText): LocalDate =
        LocalDate.parse("" + edt.text, dateFormatter)

    fun toFloat(edt: EditText): Float =
        numberFormat.parse("" + edt.text)!!.toFloat()

}