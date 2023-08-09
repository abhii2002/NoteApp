package com.example.noteapp.activities



import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ActivityAddNoteBinding
import com.example.noteapp.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val sharedViewModel : SharedViewModel by viewModels()
    private lateinit var textOptionsDialog : Dialog


    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        textOptionsDialog = Dialog(this)
        setUpCategorySpinner()

        binding.addButton.setOnClickListener {
            when {
                binding.etNoteTitle.text.isNullOrEmpty() ->{
                    Toast.makeText(this, "Please enter note title", Toast.LENGTH_SHORT).show()
                }

                binding.etNoteDescription.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter note description", Toast.LENGTH_SHORT).show()
                }else -> {
                saveNoteDetails()
                onBackPressed()
                }
            }

        }


        binding.backButton.setOnClickListener {
            onBackPressed()
        }


    }


    private fun saveNoteDetails(){


                   val noteTitle = binding.etNoteTitle.text.toString()
                   val noteDescription = binding.etNoteDescription.text.toString()
                   val categoryName = spinner.selectedItem.toString()

                   val date = Calendar.getInstance().time
                   val formatedDate = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(date)

                   val note = NoteModel(
                       title = noteTitle,
                       noteDescription = noteDescription,
                       category = categoryName,
                       date = formatedDate,
                   )
                   // save the details to the room database
                    sharedViewModel.insertNote(note)

                   sharedViewModel.response.observe(this){

                      // sharedViewModel.setSavedKey(true)
                       Toast.makeText(this, "Note Created", Toast.LENGTH_LONG).show()
                   }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }


    private fun setUpCategorySpinner(){
        spinner = binding.categorySpinner
        val categoriesArrayString = R.array.categories
        var adapter : ArrayAdapter<CharSequence>  = ArrayAdapter.createFromResource(this, categoriesArrayString, R.layout.spinner_text_view)

        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }





}