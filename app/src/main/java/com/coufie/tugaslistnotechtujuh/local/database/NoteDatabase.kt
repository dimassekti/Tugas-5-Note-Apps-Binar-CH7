package com.coufie.tugaslistnotechtujuh.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coufie.tugaslistnotechtujuh.local.dao.NoteDao
import com.coufie.tugaslistnotechtujuh.local.model.Note
import com.coufie.tugaslistnotechtujuh.local.model.User

@Database(entities = [Note::class, User::class], version = 2)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun noteDao() : NoteDao

    companion object{
        private var INSTANCE : NoteDatabase? = null
        fun getInstance(context : Context): NoteDatabase? {
            if (INSTANCE == null){
                synchronized(NoteDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java,"NoteApp.db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}