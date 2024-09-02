package br.com.petscrud.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.databinding.PetLayoutBinding
import br.com.petscrud.models.Pet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class PetActivity : AppCompatActivity() {

    private lateinit var binding: PetLayoutBinding
    private var listaPets: List<Pet> = emptyList()
    private var petUrl = "https://pet-cp-backend-default-rtdb.firebaseio.com/pets.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PetLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val gson = Gson()

        binding.apply {
            btnGravar.setOnClickListener {  }
            btnPesquisar.setOnClickListener {  }
        }
    }
}