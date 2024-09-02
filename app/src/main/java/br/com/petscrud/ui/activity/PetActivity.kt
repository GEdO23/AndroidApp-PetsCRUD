package br.com.petscrud.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.petscrud.R

class PetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_layout)
    }
}