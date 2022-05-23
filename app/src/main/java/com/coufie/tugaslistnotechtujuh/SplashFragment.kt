package com.coufie.tugaslistnotechtujuh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.datastore.UserManager


class SplashFragment : Fragment() {

    lateinit var userManager: UserManager
    lateinit var username : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireActivity())

        userManager.userUsername.asLiveData().observe(requireActivity(), {
            username = it.toString()
        })


        Handler(Looper.getMainLooper()).postDelayed({

            if(username != ""){
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
            }
//            startActivity(Intent(this, LoginActivity::class.java))


        }, 3000)
    }


}