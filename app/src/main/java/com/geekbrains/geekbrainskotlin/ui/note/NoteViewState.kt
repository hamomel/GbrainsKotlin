package com.geekbrains.geekbrainskotlin.ui.note

import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewState

class NoteViewState(data: NoteData = NoteData(),
                    error: Throwable? = null) : BaseViewState<NoteViewState.NoteData>(data, error) {

    data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
}