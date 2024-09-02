package br.com.petscrud.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.R
import br.com.petscrud.databinding.PetLayoutBinding
import br.com.petscrud.models.Pet
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.time.LocalDate

const val DB_REFERENCE_PETS = "pets"
const val RTDB_URL = "https://pet-cp-backend-default-rtdb.firebaseio.com/"

class PetActivity : AppCompatActivity() {

    private lateinit var binding: PetLayoutBinding
    private var listaPets: List<Pet> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PetLayoutBinding.inflate( layoutInflater )
        val defaultPet = Pet("", "", 0f, LocalDate.now())

        binding.pet = defaultPet
        defaultPet.peso.toString()

        setContentView(binding.root)



//        val gson = Gson()
//        val mediaType = "application/json".toMediaTypeOrNull()


        /*val database = Firebase.database
        val myRef = database.getReference(DB_REFERENCE_PETS)
        myRef.setValue("Hello, World!")*/
    }

    private fun paraEntidade(): Pet {
        //TODO: Transformar valores dos campos em uma entidade Pet
        return Pet(
            "",
            "",
            5.0f,
            LocalDate.now()
        )
    }
}