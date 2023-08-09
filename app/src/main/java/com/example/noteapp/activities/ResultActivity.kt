package com.example.noteapp.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.TooltipCompat
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ActivityResultBinding
import com.example.noteapp.databinding.DeleteBottomSheetDialogBinding

import com.example.noteapp.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private val sharedViewModel : SharedViewModel by viewModels()
    private lateinit var noteModel: NoteModel



    private lateinit var binding: ActivityResultBinding
    private  var noteTitle: String = ""
    private var noteDescription: String = ""
    private var noteCategory: String = ""
    private var formatedDate: String = ""
    private var id: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = Gson()
       noteModel = gson.fromJson<NoteModel>(intent.getStringExtra("note_model"), NoteModel::class.java)

        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)






         id = intent.getIntExtra("id", 0)
         noteTitle = intent.getStringExtra("note_title").toString()
         noteDescription = intent.getStringExtra("note_description").toString()
        noteCategory = intent.getStringExtra("note_category").toString()
        formatedDate = intent.getStringExtra("note_date").toString()





        var spinner = binding.categorySpinner
        var adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_text_view)

        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

        var spinnerPosition = 0
        spinnerPosition = adapter.getPosition(noteCategory)
        spinner.setSelection(spinnerPosition)






        binding.buttonDelete.setOnClickListener{
            setUpBottomSheetDialog()
//            deleteTask()
//            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
//            finishAffinity()

        }

        binding.updateButton.setOnClickListener{
            updateNote()
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            finishAffinity()

        }



        binding.etNoteTitle.setText(noteModel.title)
        binding.etNoteDescription.setText(noteModel.noteDescription)
        binding.tvDate.text = noteModel.date







    }


    private fun deleteTask(){
        sharedViewModel.deleteTask(noteModel)

        }

    private fun updateNote(){
        val title = binding.etNoteTitle.text.toString()
        val noteDescription = binding.etNoteDescription.text.toString()
        val noteCategory = binding.categorySpinner.selectedItem.toString()
        val formatedDate = binding.tvDate.text.toString()
        val updatedNote = NoteModel(id, title, noteDescription, noteCategory, formatedDate)
        sharedViewModel.updateNote(updatedNote)

      }

    private fun setUpBottomSheetDialog(){
         val bottomSheet: BottomSheetDialog = BottomSheetDialog(this@ResultActivity, R.style.BottomSheetStyle)
         bottomSheet.setContentView(R.layout.delete_bottom_sheet_dialog)


        val tvYes : TextView? =  bottomSheet.findViewById<TextView>(R.id.tv_yes)
        val tvNo : TextView? =  bottomSheet.findViewById<TextView>(R.id.tv_no)


        tvYes?.setOnClickListener {
             deleteTask()
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            finishAffinity()



        }

       tvNo?.setOnClickListener {
          bottomSheet.dismiss()
       }


        bottomSheet.show()
    }

    }

