package com.geekbrains.geekbrainskotlin.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.data.model.Note

class MainAdapter : RecyclerView.Adapter<NoteViewHolder>() {

    var notes: List<Note> = listOf<Note>()
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

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int): Unit {
        holder.bind(notes[position])
    }
}

class NoteViewHolder(itemViev: View) : RecyclerView.ViewHolder(itemViev) {
    private val title = itemViev.findViewById<TextView>(R.id.title)
    private val body = itemViev.findViewById<TextView>(R.id.body)

    fun bind(note: Note) {
        title.text = note.title
        body.text = note.note
        itemView.setBackgroundColor(note.color)
    }
}