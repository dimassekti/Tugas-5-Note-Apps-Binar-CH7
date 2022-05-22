package com.coufie.tugaslistnotechtujuh.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context){

    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "user_prefs")

    companion object{
        val ID = preferencesKey<String>("USER_ID")
        val PASSWORD = preferencesKey<String>("USER_PASSWORD")
        val USERNAME = preferencesKey<String>("USER_USERNAME")

    }

    suspend fun saveData(id: String, password:String, username:String){
        dataStore.edit{
            it[ID] = id
            it[PASSWORD] = password
            it[USERNAME] = username

        }
    }

    val userId : Flow<String> = dataStore.data.map {
        it[ID] ?: ""
    }


    val userPassword : Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val userUsername : Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }



    suspend fun clearData(){
        dataStore.edit {
            it.clear()
        }
    }
}