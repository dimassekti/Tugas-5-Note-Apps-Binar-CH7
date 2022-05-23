package com.coufie.tugaslistnotechtujuh

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.datastore.UserManager
import com.coufie.tugaslistnotechtujuh.local.database.NoteDatabase
import com.coufie.tugaslistnotechtujuh.local.database.UserDatabase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    var userDb : UserDatabase?= null

    lateinit var userManager: UserManager

    lateinit var id : String
    lateinit var username : String
    lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userManager = UserManager(requireContext())

        logout()
        initUM()

        btn_update_profile.setOnClickListener {
            val newUsername = et_update_username.text.toString()
            val newPassword = et_update_password.text.toString()

//            val updateNow = userDb?.UserDao()?.updateUser(newUsername, newPassword, id)
        }
    }

    fun initUM(){
        userManager.userId.asLiveData().observe(viewLifecycleOwner, {
            id = it.toString()
//            Toast.makeText(this, "ini $id", Toast.LENGTH_SHORT).show()
        })

        userManager.userPassword.asLiveData().observe(viewLifecycleOwner, {
            password = it.toString()
            et_update_password.hint = password
        })

        userManager.userUsername.asLiveData().observe(viewLifecycleOwner, {
            username = it.toString()
            et_update_username.hint = username
        })
    }

    fun logout(){
        btn_logout.setOnClickListener {
            GlobalScope.launch {
                userManager.clearData()

            }
            onDestroy()
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_profileFragment_to_loginFragment) }
        }
    }

}