package com.example.noteapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: NoteRepository,

) : ViewModel() {

    /****
     * Insert user details
     */

    private val _response = MutableLiveData<Long>()
    val response: LiveData<Long> = _response

    //insert note details to room databse
    fun insertNote(note: NoteModel){
        viewModelScope.launch(Dispatchers.IO) {
            _response.postValue(repository.addTask(note))
        }
    }

    // delete a note
     fun deleteTask(note: NoteModel) {
          viewModelScope.launch {
              val notemodel = NoteModel(
                  id = note.id,
                  title = note.title,
                  noteDescription = note.noteDescription,
                  category = note.category,
                  date = note.date,
              )
              repository.deleteTask(notemodel)
          }
        }


    fun updateNote(note: NoteModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTask(note)
        }
    }

    fun deleteAllNotes(){
         viewModelScope.launch(Dispatchers.IO) {
              repository.deleteAllTasks()
         }
    }


    /*****
     * Dispatchers Io means we a want to run this query from background thread.
     * And viewModelScope is a kotlin coroutine
     *****/



    private val _noteDetails = MutableStateFlow<List<NoteModel>>(emptyList())
    val noteDetails: StateFlow<List<NoteModel>> = _noteDetails


    fun getNoteDetails(){
         viewModelScope.launch(Dispatchers.IO) {
             repository.getAllNotes
                 .catch {  e->
                     //Log errror here
                 }
                 .collect {
                     _noteDetails.value = it
                 }
         }
    }

   fun searchDatabase(searchQuery: String): LiveData<List<NoteModel>> {
         return repository.searchDatabase(searchQuery).asLiveData()
   }


    fun getNoteCategory(note: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImportantNote(note)
                .catch {  e->
                    //Log errror here
                }
                .collect {
                    _noteDetails.value = it
                }
        }
    }


}