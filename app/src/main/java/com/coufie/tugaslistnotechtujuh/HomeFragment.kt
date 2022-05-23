package com.coufie.tugaslistnotechtujuh

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class HomeFragment : Fragment() {

//    lateinit var prefs : SharedPreferences
    var noteDb : NoteDatabase? = null
    lateinit var userManager : UserManager

    var username = "null"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        prefs = requireContext().getSharedPreferences("SF", Context.MODE_PRIVATE)

        userManager = UserManager(requireContext())
        userManager.userUsername.asLiveData().observe(requireActivity(), {
            username = it.toString()
            tv_header.setText("$username")
        })


        noteDb = NoteDatabase.getInstance(requireContext())

        getNoteData()

//        val usernameIn = prefs.getString("USERNAME", "")
//        val passwordIn = prefs.getString("PASSWORD", "")
        
        tv_logout.setOnClickListener {
//            prefs.edit().clear().apply()

            val ADBuilder = AlertDialog.Builder(it.context)
                .setTitle("Log out")
                .setMessage("Yakin Logout?")
                .setPositiveButton("Ya"){ dialogInterface: DialogInterface, i: Int ->
                    GlobalScope.launch {
                        userManager.clearData()

                        requireActivity().runOnUiThread(){
                            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment)
                        }
                    }
                }
                .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()



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

                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = current.format(formatter)

                        noteDb?.noteDao()?.insertNote(Note(null, judul, catatan, formatted))

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


        GlobalScope.launch {
            val listData = noteDb?.noteDao()?.getAllNote()
            activity?.runOnUiThread {
                if(listData?.size!! < 1){
                    tv_kosong.setText("Belum ada catatan")
                }
                listData.let {
                    rv_note.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    rv_note.adapter = NoteAdapter(it!!){
                        val noteBundle = bundleOf("NOTEDETAIL" to it)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, noteBundle)
                    }
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