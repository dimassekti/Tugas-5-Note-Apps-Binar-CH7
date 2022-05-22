package com.coufie.tugaslistnotechtujuh.local.dao

import androidx.room.*
import com.coufie.tugaslistnotechtujuh.local.model.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User) : Long

    @Query("SELECT * FROM User")
    fun getAllUser() : List<User>

    @Query("SELECT * FROM User WHERE username = :username")
    fun findUser(username : String) : List<User>

    @Query("SELECT username FROM User " +
            "WHERE User.username = :username AND User.password = :password")
    fun checkLogin(username: String, password : String) : String

    @Delete
    fun deleteUser(user: User) : Int

//    @Query("UPDATE User SET " +
//            "username = :username, " +
//            "password = :password " +
//            "WHERE id = :id")
//    fun updateUser(username: String, password: String, id : Int) : List<User>



}