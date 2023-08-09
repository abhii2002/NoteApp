package com.example.noteapp.activities


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
import com.example.adapter.CategoryButtonAdapter
import com.example.adapter.NoteItemAdapter
import com.example.noteapp.R
import com.example.noteapp.data.models.CategoryButtonModel
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val sharedViewModel : SharedViewModel by viewModels()
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
        binding.toolbarMainActivity.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.baseline_more_vert_24));

        val categoryButtonData = listOf(
            CategoryButtonModel("All", 1),
            CategoryButtonModel("Work", 1),
            CategoryButtonModel("Reading", 1),
            CategoryButtonModel("Important", 1),
        )

        //setting up the category button recycler view
        val categoryButtonAdapter = CategoryButtonAdapter(categoryButtonData)

        binding.rvCategory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryButtonAdapter
      //  categoryButtonAdapter.setData(categoryButtonData)

         getAllNotes()

        categoryButtonAdapter.setOnItemClickListener(object: CategoryButtonAdapter.onRecyclerViewItemClickListener{
            override fun onItemClickListener(
                view: View?,
                position: Int,
                oldPosition: Int,
                categoryModel: CategoryButtonModel
            ) {

                if(position == 0){
                    getAllNotes()
                }else if(position == 1){
                    generalData("Work")
                }
                else if(position == 2){
                    generalData("Reading")
                }else if(position == 3){
                    generalData("Important")
                }
            }

        })

        binding.rvMain.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvMain.adapter = adapter



        binding.addTaskFab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    searchNotes(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null){
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
        when(item.itemId){
            R.id.menu_main_setting -> {
               setUpBottomSheetDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteAllNotes(){
         sharedViewModel.deleteAllNotes()


    }

    private fun setUpBottomSheetDialog(){
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this@MainActivity, R.style.BottomSheetStyle)
        bottomSheet.setContentView(R.layout.delete_bottom_sheet_dialog)


        val tvYes : TextView? =  bottomSheet.findViewById<TextView>(R.id.tv_yes)
        val tvNo : TextView? =  bottomSheet.findViewById<TextView>(R.id.tv_no)
        var alertText : TextView? = bottomSheet.findViewById(R.id.tv_alert_text)

        alertText!!.text = resources.getString(R.string.alert_delete_all_notes)


        tvYes?.setOnClickListener {
           deleteAllNotes()
            bottomSheet.dismiss()
            recreate()
            binding.animNoTask.visibility = View.VISIBLE
            binding.rvMain.visibility = View.GONE
        }

        tvNo?.setOnClickListener {
            bottomSheet.dismiss()
        }


        bottomSheet.show()
    }

  private fun searchNotes(query: String){

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

    private fun generalData(category: String){
        this.lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                 sharedViewModel.getNoteCategory(category)
                sharedViewModel.noteDetails.collect { notes ->
                    for (note in notes) {
                        adapter.setData(notes)
                        // setting up the note data recyclerview

                    }
                }
            }
        }
    }

    private fun getAllNotes(){
        this.lifecycleScope.launch{
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.getNoteDetails()
                sharedViewModel.noteDetails.collect { notes ->
                    if(notes.isEmpty()){
                        binding.animNoTask.visibility = View.VISIBLE
                        binding.rvMain.visibility = View.GONE
                    }else {
                        for (note in notes){
                            adapter.setData(notes)
                        binding.animNoTask.visibility = View.GONE
                        binding.rvMain.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}