package br.com.petscrud.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.R
import br.com.petscrud.models.Pet
import br.com.petscrud.utils.PetUtil
import com.google.gson.Gson
import okhttp3.OkHttpClient

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

}