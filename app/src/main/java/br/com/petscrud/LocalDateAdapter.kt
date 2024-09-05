package br.com.petscrud

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter :
    JsonSerializer<LocalDate>,
    JsonDeserializer<LocalDate> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement =
        JsonPrimitive(src?.format(dateFormatter))


    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? =
        json?.asString?.let { date ->
            LocalDate.parse(date, dateFormatter)
        }
}