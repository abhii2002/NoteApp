package com.example.noteapp.activities


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.query
import com.example.ScreenState
import com.example.adapter.CategoryButtonAdapter
import com.example.adapter.NoteItemAdapter
import com.example.noteapp.R
import com.example.noteapp.RequestState
import com.example.noteapp.data.models.CategoryButtonModel
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var mProgressDialog: Dialog
    private val adapter: NoteItemAdapter by lazy { NoteItemAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.addTaskFab.shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(100.0f)

        val searchText = binding.searchView
        searchText.drawingCacheBackgroundColor = Color.CYAN

        setSupportActionBar(binding.toolbarMainActivity)
        binding.toolbarMainActivity.setOverflowIcon(
            ContextCompat.getDrawable(
                getApplicationContext(),
                R.drawable.baseline_more_vert_24
            )
        )

        /**
         * Here we are using the getNoteDetails method which directly collected all of our notes
         * in the SharedViewModel class using the viewModelScope
         */
        sharedViewModel.getNoteDetails()

        /*
         emit and collect. Think emit is similar to live data postValue and collect is similar to observe
         */
        /**
         * here we are collecting notes inside the activity itself using the lifeCycleScope (required for activities)
         * and setting the values to the adapter.
         */
        this.lifecycleScope.launch {
            sharedViewModel.noteDetails.collect { notes ->
                getAllNotes(notes)
            }
        }

        binding.rvMain.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvMain.adapter = adapter

        val categoryButtonData = listOf(
            CategoryButtonModel("All", 1),
            CategoryButtonModel("Work", 1),
            CategoryButtonModel("Reading", 1),
            CategoryButtonModel("Important", 1),
        )

        //setting up the category button recycler view
        val categoryButtonAdapter = CategoryButtonAdapter(categoryButtonData)

        binding.rvCategory.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryButtonAdapter
        //  categoryButtonAdapter.setData(categoryButtonData)


        categoryButtonAdapter.setOnItemClickListener(object :
            CategoryButtonAdapter.onRecyclerViewItemClickListener {
            override fun onItemClickListener(
                view: View?,
                position: Int,
                oldPosition: Int,
                categoryModel: CategoryButtonModel
            ) {

                if (position == 0) {
                  sharedViewModel.getNoteDetails()
                } else if (position == 1) {
                    sharedViewModel.getNoteCategory("Work")
                } else if (position == 2) {
                    sharedViewModel.getNoteCategory("Reading")
                } else if (position == 3) {
                    sharedViewModel.getNoteCategory("Important")
                }
            }

        })


        binding.addTaskFab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchNotes(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchNotes(query)
                }
                return true
            }

        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.noteapp.R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_setting -> {
                setUpBottomSheetDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteAllNotes() {
        sharedViewModel.deleteAllNotes()
    }

    private fun setUpBottomSheetDialog() {
        val bottomSheet: BottomSheetDialog =
            BottomSheetDialog(this@MainActivity, R.style.BottomSheetStyle)
           bottomSheet.setContentView(R.layout.delete_bottom_sheet_dialog)


        val tvYes: TextView? = bottomSheet.findViewById<TextView>(R.id.tv_yes)
        val tvNo: TextView? = bottomSheet.findViewById<TextView>(R.id.tv_no)
        var alertText: TextView? = bottomSheet.findViewById(R.id.tv_alert_text)

        alertText!!.text = resources.getString(R.string.alert_delete_all_notes)


        tvYes?.setOnClickListener {
            deleteAllNotes()
            bottomSheet.dismiss()
            binding.animNoTask.visibility = View.VISIBLE
            binding.rvMain.visibility = View.GONE
        }

        tvNo?.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }

    private fun searchNotes(query: String) {

        val searchQuery = "%$query%"
        sharedViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }
        }
    }


    /*
    repeatOnLifecycle is a suspend function. As such, it needs to be executed within a coroutine
     */


    private fun getAllNotes(state : RequestState<List<NoteModel>>){
        when(state){
            is RequestState.Loading -> {
             showDialog()

            }

            is RequestState.Success ->{
                hideDialog()

                if (state.data.isEmpty()){
                    binding.animNoTask.visibility = View.VISIBLE
                    binding.rvMain.visibility = View.GONE
                }else {

                    adapter.setData(state.data)
                    binding.animNoTask.visibility = View.GONE
                    binding.rvMain.visibility = View.VISIBLE
                }

            }

            is RequestState.Idle ->{

            }
        }
    }

    fun showDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideDialog(){
        mProgressDialog.hide()

    }

}