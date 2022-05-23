package com.coufie.tugaslistnotechtujuh

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.coufie.tugaslistnotechtujuh.datastore.UserManager
import com.coufie.tugaslistnotechtujuh.local.database.NoteDatabase
import com.coufie.tugaslistnotechtujuh.local.model.Note
import kotlinx.android.synthetic.main.custom_dialog_edit_note.view.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DetailFragment : Fragment() {

    private var noteDb : NoteDatabase? = null
    private lateinit var UserManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        noteDb = NoteDatabase.getInstance(requireActivity())
        UserManager = UserManager(requireActivity())
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteDb = NoteDatabase.getInstance(requireActivity())
        UserManager = UserManager(requireActivity())

        val noteDetail = arguments?.getParcelable<Note>("NOTEDETAIL") as Note

        tv_detail_content.text = noteDetail.content
        tv_detail_time.text = noteDetail.time
        tv_detail_title.text = noteDetail.title

        iv_detail_delete.setOnClickListener {

            val ADBuilder = AlertDialog.Builder(it.context)
                .setTitle("Hapus Data")
                .setMessage("Yakin Hapus?")
                .setPositiveButton("Ya"){ dialogInterface: DialogInterface, i: Int ->
                    val result = noteDb?.noteDao()?.deleteNote(noteDetail)
                    if(result == 1){
                        Toast.makeText(requireContext(),"Note Terhapus",Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView()).navigate(R.id.action_detailFragment_to_homeFragment)

                    }
                }
                .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .show()
        }

        iv_detail_edit.setOnClickListener {

            val customDialogEdit = LayoutInflater.from(it.context).inflate(R.layout.custom_dialog_edit_note, null, false)

//            edit data
            val ADBuilder = AlertDialog.Builder(it.context)
                .setView(customDialogEdit)
                .create()

//            custom dialog untuk edit
            customDialogEdit.btn_update_note.setOnClickListener {

                if(customDialogEdit.et_update_catatan.text.length > 0 && customDialogEdit.et_update_judul.text.length > 0){

                    val newTitle = customDialogEdit.et_update_judul.text.toString()
                    val newContent = customDialogEdit.et_update_catatan.text.toString()

                    val result = noteDb?.noteDao()?.updateNote(Note(noteDetail.id, newTitle, newContent, noteDetail.time))

                    (customDialogEdit.context as MainActivity).runOnUiThread(){
                        if(result != 0){
                            Toast.makeText(it.context, "Data Berhasil diupdate", Toast.LENGTH_LONG).show()
                            noteDetail.content = newContent
                            noteDetail.title = newTitle
                            ADBuilder.dismiss()
                            (customDialogEdit.context as MainActivity).recreate()

                        }else{

                        }
                        (customDialogEdit.context as MainActivity).recreate()

                    }

                }else{
                    Toast.makeText(it.context, "Mohon input data secara lengkap", Toast.LENGTH_LONG).show()
                }
            }
            ADBuilder.show()
        }

        iv_detail_share.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,noteDetail.content)
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }



    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        NoteDatabase.destroyInstance()
    }


}