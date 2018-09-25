package com.geekbrains.geekbrainskotlin.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.extensions.getColorInt

class MainAdapter(private val onItemClickListener: (note: Note) -> Unit) : RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    inner class NoteViewHolder(itemViev: View) : RecyclerView.ViewHolder(itemViev) {
        private val titleView = itemViev.findViewById<TextView>(R.id.title)
        private val bodyView = itemViev.findViewById<TextView>(R.id.body)

        fun bind(note: Note) {
            with(note) {
                titleView.text = title
                bodyView.text = body

                itemView.setBackgroundColor(color.getColorInt(itemView.context))
                itemView.setOnClickListener { onItemClickListener(note) }
            }
        }
    }
}