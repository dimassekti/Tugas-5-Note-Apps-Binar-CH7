package com.coufie.tugaslistnotechtujuh

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.local.database.UserDatabase
import com.coufie.tugaslistnotechtujuh.datastore.UserManager
import com.coufie.tugaslistnotechtujuh.local.model.User
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var userDb : UserDatabase? = null

//    lateinit var prefs : SharedPreferences
    lateinit var userManager: UserManager

    var id = ""
    var password = ""
    var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        //sf
//        prefs = requireContext().getSharedPreferences("SF", Context.MODE_PRIVATE)

        userManager.userUsername.asLiveData().observe(viewLifecycleOwner, {
            username = it.toString()
        })

        if(username != ""){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        }


// sf
//        if(requireContext().getSharedPreferences("SF", Context.MODE_PRIVATE).contains("EMAIL") && requireContext().getSharedPreferences("SF", Context.MODE_PRIVATE).contains("PASSWORD")){
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
//        }

        btn_login.setOnClickListener {
            login()
        }

        tv_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }


    fun login(){


        if(et_input_username.text.isNotEmpty() && et_input_password.text.isNotEmpty()){

            userDb = UserDatabase.getInstance(requireContext())

            val dataUsername = et_input_username.text.toString()
            val dataPassword = et_input_password.text.toString()

            val userCheck = userDb?.UserDao()?.checkLogin(dataUsername, dataPassword)

            if (userCheck.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Username atau Password Masih Salah", Toast.LENGTH_LONG).show()
            }else{

//                prefs.edit().putString("PASSWORD", dataPassword).apply()
//                prefs.edit().putString("USERNAME", username).apply()
                GlobalScope.launch {

                    userManager.saveData(id.toString(), dataPassword, dataUsername)
                    Log.d("tesuser", dataUsername)

                    val result = userDb?.UserDao()?.insertUser(User(null, username, password ))

                    activity?.runOnUiThread{
                        if(result != 0.toLong()){
                            Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_LONG).show()
                            view?.let { Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_homeFragment) }
                        }else{
                            Toast.makeText(requireContext(), "Login Gagal", Toast.LENGTH_LONG).show()
                        }
                    }

                }

                view?.let { Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_homeFragment) }
            }

        }else{
            Toast.makeText(requireContext(), "Username atau Password Masih Salah", Toast.LENGTH_LONG).show()
        }
    }


}