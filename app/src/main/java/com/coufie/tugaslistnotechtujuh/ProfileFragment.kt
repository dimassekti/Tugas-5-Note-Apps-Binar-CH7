package com.coufie.tugaslistnotechtujuh

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.datastore.UserManager
import com.coufie.tugaslistnotechtujuh.local.database.NoteDatabase
import com.coufie.tugaslistnotechtujuh.local.model.User
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    var noteDb : NoteDatabase?= null

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


        btn_home.setOnClickListener {
            requireActivity().runOnUiThread(){
                view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_profileFragment_to_homeFragment) }
            }
        }


        btn_update_profile.setOnClickListener {

            var newUsername = username
            var newPassword = password

            if(et_update_password.text.toString() == "" && et_update_username.text.isNotEmpty()){
                newUsername = et_update_username.text.toString()
                et_update_username.hint = newUsername
                Toast.makeText(requireContext(), "Update Username Berhasil", Toast.LENGTH_LONG).show()
            }else if(et_update_username.text.toString() == "" && et_update_password.text.isNotEmpty()){
                newPassword = et_update_password.text.toString()
                et_update_password.hint = newPassword
                Toast.makeText(requireContext(), "Update Password Berhasil", Toast.LENGTH_LONG).show()

            }else if(et_update_password.text.toString() == "" && et_update_username.text.toString() == ""){
                Toast.makeText(requireContext(), "Mohon isi data", Toast.LENGTH_LONG).show()
            }else{
                newUsername = et_update_username.text.toString()
                newPassword = et_update_password.text.toString()
                et_update_username.hint = newUsername
                et_update_password.hint = newPassword
                Toast.makeText(requireContext(), "Update Username & Password Berhasil", Toast.LENGTH_LONG).show()
            }

            val update = noteDb?.noteDao()?.updateUser(User(id.toInt(), newUsername, newPassword))
            et_update_password.hint = password
            et_update_username.hint = username
            GlobalScope.launch {
                userManager.saveData(id.toString(), newPassword, newUsername)
            }
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
            val ADBuilder = AlertDialog.Builder(it.context)
                .setTitle("Log out")
                .setMessage("Yakin Logout?")
                .setPositiveButton("Ya"){ dialogInterface: DialogInterface, i: Int ->
                    GlobalScope.launch {
                        userManager.clearData()

                        requireActivity().runOnUiThread(){
                            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_homeFragment_to_loginFragment) }
                        }
                    }
                }
                .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()

        }
    }

}