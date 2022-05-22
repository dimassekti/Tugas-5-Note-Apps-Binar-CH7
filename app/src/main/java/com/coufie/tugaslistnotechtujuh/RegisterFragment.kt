package com.coufie.tugaslistnotechtujuh

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.local.database.UserDatabase
import com.coufie.tugaslistnotechtujuh.local.model.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class RegisterFragment : Fragment() {

    var userDb : UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register.setOnClickListener {
            registerUser()

        }

        tv_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun registerUser(){

        userDb = UserDatabase.getInstance(requireContext())

        if(et_username.text.isNullOrEmpty()
            && et_password.text.isNullOrEmpty()
            && et_password_confirm.text.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Mohon isi semua data dengan lengkap", Toast.LENGTH_LONG).show()
        }
        else{
            if(et_password.text.toString() != et_password_confirm.text.toString()){
                Toast.makeText(requireContext(), "Password tidak sama", Toast.LENGTH_LONG).show()
            }else{
                GlobalScope.async {
                    val username = et_username.text.toString()
                    val password = et_password.text.toString()

                    val result = userDb?.UserDao()?.insertUser(User(null, username, password ))

                    activity?.runOnUiThread{
                        if(result != 0.toLong()){
                            Toast.makeText(requireContext(), "Register Berhasil", Toast.LENGTH_LONG).show()
                            view?.let { Navigation.findNavController(it).navigate(R.id.action_registerFragment_to_loginFragment) }
                        }else{
                            Toast.makeText(requireContext(), "Register Gagal", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        }

    }

}