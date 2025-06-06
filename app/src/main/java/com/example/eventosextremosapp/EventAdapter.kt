package com.example.eventosextremosapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para o RecyclerView que exibe a lista de eventos extremos
 */
class EventAdapter(private val context: Context, private val events: MutableList<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    /**
     * Interface para lidar com cliques no botão de excluir
     */
    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    private var deleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.deleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)

        holder.btnDelete.setOnClickListener {
            deleteClickListener?.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = events.size

    /**
     * Adiciona um novo evento à lista
     */
    fun addEvent(event: Event) {
        events.add(event)
        notifyItemInserted(events.size - 1)
    }

    /**
     * Remove um evento da lista
     */
    fun removeEvent(position: Int) {
        if (position >= 0 && position < events.size) {
            events.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, events.size)
        }
    }

    /**
     * ViewHolder para os itens do RecyclerView
     */
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLocalName: TextView = itemView.findViewById(R.id.tvLocalName)
        private val tvEventType: TextView = itemView.findViewById(R.id.tvEventType)
        private val tvImpactLevel: TextView = itemView.findViewById(R.id.tvImpactLevel)
        private val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
        private val tvAffectedPeople: TextView = itemView.findViewById(R.id.tvAffectedPeople)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(event: Event) {
            tvLocalName.text = event.localName
            tvEventType.text = event.eventType
            tvImpactLevel.text = event.impactLevel
            tvEventDate.text = event.eventDate
            tvAffectedPeople.text = event.affectedPeople.toString()

            // Define a cor de fundo do nível de impacto
            val backgroundColor = when (event.impactLevel.lowercase()) {
                "leve" -> R.color.impact_light
                "moderado" -> R.color.impact_moderate
                "severo" -> R.color.impact_severe
                else -> R.color.impact_moderate
            }
            tvImpactLevel.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
        }
    }
}