package com.coufie.tugaslistnotechtujuh.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coufie.tugaslistnotechtujuh.local.dao.UserDao
import com.coufie.tugaslistnotechtujuh.local.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun UserDao() : UserDao

    companion object{
        private var INSTANCE : UserDatabase? = null
        fun getInstance(context : Context): UserDatabase? {
            if (INSTANCE == null){
                synchronized(NoteDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java,"User.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}