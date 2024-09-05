package br.com.petscrud.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.LocalDateAdapter
import br.com.petscrud.R
import br.com.petscrud.models.Pet
import br.com.petscrud.utils.LocalDateUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.time.LocalDate

private const val RTDB_URL = "https://pet-cp-backend-default-rtdb.firebaseio.com/pets.json"

class PetActivity : AppCompatActivity() {
    private var listaPets: MutableList<Pet> = mutableListOf()
    private val httpClient = OkHttpClient()
    private val gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDate::class.java,
            LocalDateAdapter()
        ).create()

    private lateinit var petNome: EditText
    private lateinit var petRaca: EditText
    private lateinit var petPeso: EditText
    private lateinit var petNascimento: EditText
    private lateinit var btnGravar: Button
    private lateinit var btnPesquisar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_layout)

        petNome = findViewById(R.id.pet_nome)
        petRaca = findViewById(R.id.pet_raca)
        petPeso = findViewById(R.id.pet_peso)
        petNascimento = findViewById(R.id.pet_nascimento)
        btnGravar = findViewById(R.id.btn_gravar)
        btnPesquisar = findViewById(R.id.btn_pesquisar)

        btnGravar.setOnClickListener {
            salvarFirebase()
        }
    }

    private fun paraEntidade(): Pet {
        val nome = petNome.text.toString()
        val raca = petRaca.text.toString()
        val peso = petPeso.text.toString().toFloat()
        val nascimento = LocalDateUtil()
            .toDate(petNascimento.text.toString())

        return Pet(nome, raca, peso, nascimento)
    }

    private fun salvarFirebase() {
        val petTela = paraEntidade()
        val petJson = """
        { 
            "nome": "${petTela.nome}",
            "raca": "${petTela.raca}",
            "peso": "${petTela.peso}",
            "nascimento": "${LocalDateUtil().toString(petTela.nascimento)}"
        }
        """.trimIndent()

        val mediaType = "application/json".toMediaTypeOrNull()
        //TODO Será que mantenho o nome da variável como body?
        val body = petJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(RTDB_URL)
            .post(body)
            .build()

        val response = object : Callback {
            private var responseLogTag = "SalvarFirebaseResponse"

            override fun onFailure(call: Call, e: IOException) {
                Log.e(responseLogTag, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val localBody = response.body
                //TODO Não gosto de utilizar asserted call
                Log.i(responseLogTag, localBody!!.string())

                //TODO Salvar Pet no Realtime Database

                runOnUiThread {
                    Toast.makeText(
                        this@PetActivity,
                        "Pet cadastrado com sucesso", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        httpClient.newCall(request)
            .enqueue(response)

        carregarFirebase()
    }

    private fun carregarFirebase() {
        listaPets.clear()

        val request = Request.Builder()
            .url(RTDB_URL)
            .get()
            .build()

        val response = object : Callback {
            private var responseLogTag = "CarregarFirebaseResponse"

            override fun onFailure(call: Call, e: IOException) {
                Log.e(responseLogTag, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val localBody = response.body
                    val petListJson = localBody?.string()

                    val petsMapType = object : TypeToken<Map<String, Pet>>() {}.type
                    val petsMap: Map<String, Pet> = gson.fromJson(petListJson, petsMapType)

                    val pets = petsMap.values.toList()

                    runOnUiThread {
                        pets.forEach {
                            listaPets.add(it)
                            Log.d(
                                "PetAdcionado", "O pet ${it.nome} da raça ${it.raca} " +
                                        "que pesa ${it.peso}kg " +
                                        "e nasceu em ${LocalDateUtil().toString(it.nascimento)} " +
                                        "foi adcionado à lista de pets com sucesso!"
                            )
                        }
                    }
                } catch (e: IOException) {
                    Log.e(responseLogTag, e.message.toString())
                    runOnUiThread {
                        Toast.makeText(
                            this@PetActivity,
                            "Erro ao buscar pets", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        httpClient.newCall(request)
            .enqueue(response)
    }
}