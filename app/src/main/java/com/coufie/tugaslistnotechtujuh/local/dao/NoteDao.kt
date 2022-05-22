package com.coufie.tugaslistnotechtujuh.local.dao

import androidx.room.*
import com.coufie.tugaslistnotechtujuh.local.model.Note

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: Note) : Long

    @Query("SELECT * FROM Note")
    fun getAllNote() : List<Note>

    @Delete
    fun deleteNote(note: Note) : Int

    @Update
    fun updateNote(note: Note) : Int



}