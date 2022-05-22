package com.coufie.tugaslistnotechtujuh

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.coufie.tugaslistnotechtujuh.adapter.NoteAdapter
import com.coufie.tugaslistnotechtujuh.local.database.NoteDatabase
import com.coufie.tugaslistnotechtujuh.datastore.UserManager
import com.coufie.tugaslistnotechtujuh.local.model.Note
import kotlinx.android.synthetic.main.custom_dialog_add_note.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

//    lateinit var prefs : SharedPreferences
    var noteDb : NoteDatabase? = null
    lateinit var userManager : UserManager

    lateinit var username : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        prefs = requireContext().getSharedPreferences("SF", Context.MODE_PRIVATE)

        username = "kosong"

        userManager = UserManager(requireContext())
        userManager.userUsername.asLiveData().observe(viewLifecycleOwner, {
            username = it.toString()
        })

        noteDb = NoteDatabase.getInstance(requireContext())

        getNoteData()

//        val usernameIn = prefs.getString("USERNAME", "")
//        val passwordIn = prefs.getString("PASSWORD", "")

        tv_header.setText("$username")

        tv_logout.setOnClickListener {
//            prefs.edit().clear().apply()
            onDestroy()
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
        }

        tv_profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        fab_add.setOnClickListener {

            val customDialogAdd = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_add_note, null, false)

            val ADBuilder = AlertDialog.Builder(requireContext())
                .setView(customDialogAdd)
                .create()
            customDialogAdd.btn_input_note.setOnClickListener {

                if(customDialogAdd.et_input_judul.text.length > 0 && customDialogAdd.et_input_catatan.text.length > 0){

                    GlobalScope.async {

                        val judul = customDialogAdd.et_input_judul.text.toString()
                        val catatan = customDialogAdd.et_input_catatan.text.toString()
                        noteDb?.noteDao()?.insertNote(Note(null, judul, catatan))

                    }

                    Toast.makeText(requireContext(), "Data Berhasil di Input", Toast.LENGTH_LONG).show()
                    ADBuilder.dismiss()
                    activity?.recreate()

                }else{
                    Toast.makeText(requireContext(), "Mohon input secara lengkap", Toast.LENGTH_LONG).show()
                }


            }
            ADBuilder.show()

        }
    }


    fun getNoteData(){
        rv_note.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        GlobalScope.launch {
            val listData = noteDb?.noteDao()?.getAllNote()
            activity?.runOnUiThread {
                if(listData?.size!! < 1){
                    tv_kosong.setText("Belum ada catatan")
                }
                listData.let {
                    rv_note.adapter = NoteAdapter(it!!)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        getNoteData()
    }

    override fun onDestroy() {
        super.onDestroy()
        NoteDatabase.destroyInstance()
    }

}