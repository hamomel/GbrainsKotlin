package com.geekbrains.geekbrainskotlin.ui.note

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.geekbrains.geekbrainskotlin.R
import com.geekbrains.geekbrainskotlin.data.model.Color
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.extensions.format
import com.geekbrains.geekbrainskotlin.extensions.getColorInt
import com.geekbrains.geekbrainskotlin.ui.base.BaseActivity
import com.geekbrains.geekbrainskotlin.ui.note.NoteViewState.NoteData
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

private const val SAVE_DELAY = 1000L

class NoteActivity : BaseActivity<NoteData>() {

    companion object {
        private val EXTRA_NOTE_ID = NoteActivity::class.java.name + "extra.NOTE_ID"

        fun start(context: Context, noteId: String?) =
                context.startActivity<NoteActivity>(EXTRA_NOTE_ID to noteId)
    }

    override val model: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private var color = Color.WHITE
    private val noteId by lazy { intent.getStringExtra(EXTRA_NOTE_ID) }


    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (noteId == null) {
            supportActionBar?.title = getString(R.string.new_note_title)
        } else {
            progressView.visibility = View.VISIBLE
            model.loadNote(noteId)
        }

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }

        setEditListener()
    }

    private fun setEditListener() {
        titleEt.addTextChangedListener(textChangeListener)
        titleEt.setSelection(titleEt.text.length)
        bodyEt.addTextChangedListener(textChangeListener)
        bodyEt.setSelection(bodyEt.text.length)
    }

    private fun removeEditListener() {
        titleEt.removeTextChangedListener(textChangeListener)
        bodyEt.removeTextChangedListener(textChangeListener)
    }

    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            menuInflater.inflate(R.menu.note_menu, menu).let { true }

    override fun renderData(data: NoteData) {
        if (data.isDeleted) finish()
        progressView.visibility = if (noteId != null && data.note == null) View.VISIBLE else View.GONE

        this.note = data.note
        data.note?.let { color = it.color }
        initView()
    }

    private fun initView() {
        note?.run {
            supportActionBar?.title = lastChanged.format()
            setToolbarColor(color)

            removeEditListener()
            titleEt.setText(title)
            bodyEt.setText(body)
            setEditListener()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.delete_dialog_message
            negativeButton(R.string.cancel_btn_title) { dialog -> dialog.dismiss() }
            positiveButton(R.string.ok_bth_title) { model.deleteNote() }
        }.show()
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }

    private fun triggerSaveNote() {
        if (titleEt.text.length < 3 && bodyEt.text.length < 3) return

        launch {
            delay(SAVE_DELAY)

            note = note?.copy(title = titleEt.text.toString(),
                    body = bodyEt.text.toString(),
                    lastChanged = Date(),
                    color = color)
                    ?: createNewNote()

            note?.let { model.saveChanges(it) }
        }
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
            titleEt.text.toString(),
            bodyEt.text.toString(),
            color)
}