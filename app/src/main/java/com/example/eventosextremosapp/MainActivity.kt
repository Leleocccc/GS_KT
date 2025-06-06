package com.example.eventosextremosapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventosextremosapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var eventAdapter: EventAdapter
    private val eventsList = mutableListOf<Event>()
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupDatePicker()
        setupImpactLevelDropdown()
        setupButtons()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(this, eventsList)
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }

        // Configurar o listener para o botão de excluir
        eventAdapter.setOnDeleteClickListener(object : EventAdapter.OnDeleteClickListener {
            override fun onDeleteClick(position: Int) {
                eventAdapter.removeEvent(position)
            }
        })
    }

    private fun setupDatePicker() {
        // Configurar o campo de data para abrir um DatePicker quando clicado
        binding.etEventDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Impedir edição direta do campo de data
        binding.etEventDate.keyListener = null
    }

    private fun setupImpactLevelDropdown() {
        // Criar um array adapter para o dropdown de níveis de impacto
        val impactLevels = arrayOf("Leve", "Moderado", "Severo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, impactLevels)
        binding.etImpactLevel.setAdapter(adapter)
    }

    private fun setupButtons() {
        // Configurar o botão de adicionar evento
        binding.btnAdd.setOnClickListener {
            if (validateInputs()) {
                addEvent()
            }
        }

        // Configurar o botão Sobre
        binding.btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateDateInView() {
        binding.etEventDate.setText(dateFormatter.format(calendar.time))
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validar nome do local
        if (binding.etLocalName.text.toString().trim().isEmpty()) {
            binding.tilLocalName.error = getString(R.string.error_empty_field)
            isValid = false
        } else {
            binding.tilLocalName.error = null
        }

        // Validar tipo de evento
        if (binding.etEventType.text.toString().trim().isEmpty()) {
            binding.tilEventType.error = getString(R.string.error_empty_field)
            isValid = false
        } else {
            binding.tilEventType.error = null
        }

        // Validar nível de impacto
        if (binding.etImpactLevel.text.toString().trim().isEmpty()) {
            binding.tilImpactLevel.error = getString(R.string.error_empty_field)
            isValid = false
        } else {
            binding.tilImpactLevel.error = null
        }

        // Validar data do evento
        if (binding.etEventDate.text.toString().trim().isEmpty()) {
            binding.tilEventDate.error = getString(R.string.error_empty_field)
            isValid = false
        } else {
            binding.tilEventDate.error = null
        }

        // Validar número de pessoas afetadas
        val affectedPeopleText = binding.etAffectedPeople.text.toString().trim()
        if (affectedPeopleText.isEmpty()) {
            binding.tilAffectedPeople.error = getString(R.string.error_empty_field)
            isValid = false
        } else {
            try {
                val affectedPeople = affectedPeopleText.toInt()
                if (affectedPeople <= 0) {
                    binding.tilAffectedPeople.error = getString(R.string.error_invalid_number)
                    isValid = false
                } else {
                    binding.tilAffectedPeople.error = null
                }
            } catch (e: NumberFormatException) {
                binding.tilAffectedPeople.error = getString(R.string.error_invalid_number)
                isValid = false
            }
        }

        return isValid
    }

    private fun addEvent() {
        val localName = binding.etLocalName.text.toString().trim()
        val eventType = binding.etEventType.text.toString().trim()
        val impactLevel = binding.etImpactLevel.text.toString().trim()
        val eventDate = binding.etEventDate.text.toString().trim()
        val affectedPeople = binding.etAffectedPeople.text.toString().trim().toInt()

        val event = Event(localName, eventType, impactLevel, eventDate, affectedPeople)
        eventAdapter.addEvent(event)

        // Limpar os campos após adicionar o evento
        clearInputFields()

        // Rolar para o último item adicionado
        binding.rvEvents.smoothScrollToPosition(eventsList.size - 1)

        // Mostrar mensagem de sucesso
        Toast.makeText(this, "Evento adicionado com sucesso!", Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        binding.etLocalName.text?.clear()
        binding.etEventType.text?.clear()
        binding.etImpactLevel.text?.clear()
        binding.etEventDate.text?.clear()
        binding.etAffectedPeople.text?.clear()

        // Focar no primeiro campo
        binding.etLocalName.requestFocus()
    }
}