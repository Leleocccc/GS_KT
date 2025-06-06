package com.example.eventosextremosapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eventosextremosapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar o nome do desenvolvedor
        binding.tvDeveloperName.text = "Desenvolvedor - RM12345"

        // Configurar o botão de voltar
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Configurar a ActionBar
        supportActionBar?.apply {
            title = getString(R.string.title_activity_about)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}