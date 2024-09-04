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

private const val URL = "https://pet-cp-backend-default-rtdb.firebaseio.com/pets.json"

class PetActivity : AppCompatActivity() {

    private var listaPets: List<Pet> = emptyList()
    private var client = OkHttpClient()
    private var gson = Gson()

    private lateinit var petNome: EditText
    private lateinit var petRaca: EditText
    private lateinit var petPeso: EditText
    private lateinit var petNascimento: EditText
    private lateinit var btnGravar: Button
    private lateinit var btnPesquisar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_layout)
        inicializaCampos()
    }

    private fun inicializaCampos() {
        petNome = findViewById(R.id.pet_nome)
        petRaca = findViewById(R.id.pet_raca)
        petPeso = findViewById(R.id.pet_peso)
        petNascimento = findViewById(R.id.pet_nascimento)
        btnGravar = findViewById(R.id.btn_gravar)
        btnPesquisar = findViewById(R.id.btn_pesquisar)
    }

    private fun paraEntidade(): Pet {
        val nome = "" + petNome
        val raca = "" + petRaca

        val peso = PetUtil()
            .toFloat(petPeso)

        val nascimento = PetUtil()
            .toDate(petNascimento)

        return Pet(nome, raca, peso, nascimento)
    }

    private fun paraTela(p: Pet) {
        val nome = "" + p.nome
        val raca = "" + p.raca
        val peso = "" + p.peso
        val nascimento = "" + p.nascimento

        petNome.setText(nome)
        petRaca.setText(raca)
        petPeso.setText(peso)
        petNascimento.setText(nascimento)
    }

    private fun carregarFirebase() {
        listaPets = emptyList()

        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val response = getFirebaseResponse()

        client.newCall(request)
            .enqueue(response)
    }

    private fun getFirebaseResponse(): Callback = object : Callback {
        private var TAG = "data pet firebase response"

        override fun onFailure(call: Call, e: IOException) {
            Log.e("$TAG failure", "${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val json = response.body?.string()
            Log.i("$TAG success json", "$json")

            try {
                inserirElementosNaLista(json)

            } catch (err: Exception) {
                Log.e("$TAG error", "${err.message}")
                Toast.makeText(
                    this@PetActivity,
                    "Pet não encontrado", Toast.LENGTH_LONG
                ).show()
            }
        }

        private fun inserirElementosNaLista(json: String?) {
            val elemento = getElemento(json)

            runOnUiThread {
                elemento.values.forEach { p ->
                    Log.v("$TAG success", "$p")
                    if (p != null) listaPets += p
                }

                Toast.makeText(
                    this@PetActivity,
                    "Elementos inseridos na lista", Toast.LENGTH_LONG
                ).show()
            }
        }

        private fun getElemento(json: String?): HashMap<String?, Pet?> {
            val type = object :
                TypeToken<HashMap<String?, Pet?>?>() {}.type

            val elemento: HashMap<String?, Pet?> =
                gson.fromJson(json, type)

            return elemento
        }
    }

    private fun salvarFirebase(p: Pet) {
        val mediaType = "application/json".toMediaTypeOrNull()

        val json = PetUtil().toJson(p)

        val body = json.toRequestBody(mediaType)

        Request.Builder()
            .url(URL)
            .post(body)
            .build()

        carregarFirebase()
    }

    // TODO: Pesquisar
    private fun pesquisar() {

        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val response = object : Callback {
            private var TAG = "data pet pesquisar response"

            override fun onFailure(call: Call, e: IOException) {
                Log.e("$TAG failure", "${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                Log.i("$TAG success", "$json")

                try {
                    val elementos = getElementos(json)
                    val petEncontrado = getPetEncontrado(elementos)

                    if (petEncontrado != null) {
                        paraTela(petEncontrado)
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@PetActivity,
                                "Pet não encontrado", Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    Log.e("$TAG error", "${e.message}")
                    runOnUiThread {
                        Toast.makeText(
                            this@PetActivity,
                            "Pet não encontrado", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            private fun getElementos(json: String?): List<Pet> {
                val listaPets: List<Pet> = gson
                    .fromJson(json, Array<Pet>::class.java)
                    .toList()

                return listaPets
            }

            private fun getPetEncontrado(
                list: List<Pet>?
            ): Pet? {
                val petNome = "" + petNome.text

                val petEncontrado = list?.find { pet -> pet.nome == petNome }
                Log.i("data pet findbyname", "" + petEncontrado)

                return petEncontrado
            }
        }

        client.newCall(request)
            .enqueue(response)
    }

}