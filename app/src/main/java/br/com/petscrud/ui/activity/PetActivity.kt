package br.com.petscrud.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.R
import br.com.petscrud.models.Pet
import br.com.petscrud.utils.PetUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class PetActivity : AppCompatActivity() {

    private var listaPets: MutableList<Pet> = mutableListOf()
    private var url = "https://pet-cp-backend-default-rtdb.firebaseio.com/pets.json"
    private var client = OkHttpClient()
    private var gson = Gson()

    private lateinit var petNome: EditText
    private lateinit var petRaca: EditText
    private lateinit var petPeso: EditText
    private lateinit var petNascimento: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_layout)
        inicializaCampos()

        val btnGravar: Button = findViewById(R.id.btn_gravar)
        val btnPesquisar: Button = findViewById(R.id.btn_pesquisar)

        btnGravar.setOnClickListener {
//            salvarFirebase()
            carregarFirebase()
            Log.i("lista", listaPets.toString())
        }
    }

    private fun carregarFirebase() {

        listaPets.clear()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.e("response", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val localBody = response.body
                    val textoJson = localBody?.string().toString()
                    Log.i("response json", textoJson)
//
//                    val pets = petsResponse.pets.values.toList()
//
//                    pets.forEach { Log.i("pet", it.toString()) }

                } catch (e: IOException) {
                    Log.e("erro", e.message.toString())
                }
            }
        }

        client.newCall(request)
            .enqueue(response)
    }

    private fun salvarFirebase() {

        val pet = paraEntidade()
        val jsonPet = PetUtil()
            .convertToJson(pet)

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = jsonPet.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val response = object : Callback {
            private var tag = "gravar response"

            override fun onFailure(call: Call, e: IOException) {
                tag += " failure"
                Log.e(tag, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                tag += " success"

                val localBody = response.body
                Log.i(tag, "" + localBody?.string())

                runOnUiThread {
                    val toast = Toast.makeText(
                        this@PetActivity,
                        "Pet cadastrado com sucesso", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }
        }

        client.newCall(request)
            .enqueue(response)

        carregarFirebase()
    }

    private fun paraEntidade(): Pet {
        val nome = petNome.text.toString()
        val raca = petRaca.text.toString()
        val peso = petPeso.text.toString().toFloat()
        val nascimento = PetUtil()
            .convertStringToDate(petNascimento.text.toString())

        return Pet(nome, raca, peso, nascimento)
    }

    private fun inicializaCampos() {
        petNome = findViewById(R.id.pet_nome)
        petRaca = findViewById(R.id.pet_raca)
        petPeso = findViewById(R.id.pet_peso)
        petNascimento = findViewById(R.id.pet_nascimento)
    }

}