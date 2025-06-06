package com.example.eventosextremosapp

import java.io.Serializable

/**
 * Classe que representa um evento extremo climático
 */
data class Event(
    val localName: String,
    val eventType: String,
    val impactLevel: String,
    val eventDate: String,
    val affectedPeople: Int
) : Serializable