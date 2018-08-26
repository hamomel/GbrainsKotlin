package com.geekbrains.geekbrainskotlin.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = MainAdapter( object : MainAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })
        mainRecycler.adapter = adapter

        viewModel.viewState().observe(this, Observer<MainViewState> { t ->
            t?.let { adapter.notes = it.notes }
        })

        fab.setOnClickListener { openNoteScreen(null) }
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }
}
