package com.example.noteapp.repositories

import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.data.NoteDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/*
A flow is very similar to an Iterator that produces a sequence of values, but it uses suspend functions
to produce and consume values asynchronously. This means, for example, that the flow can safely make
 a network request to produce the next value without blocking the main thread.
 */

@ViewModelScoped
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    val getAllNotes : Flow<List<NoteModel>> = noteDao.getAllNotes()



    fun getImportantNote(noteCategory: String) : Flow<List<NoteModel>>{
           return noteDao.getSelectedCategory(noteCategory)
    }

    suspend fun addTask(noteModel: NoteModel): Long{
        return noteDao.addNote(noteModel = noteModel)
    }

    suspend fun updateTask(noteModel: NoteModel){
        noteDao.updateNote(noteModel = noteModel)
    }

    suspend fun deleteTask(noteModel: NoteModel){
        noteDao.deleteTask(noteModel = noteModel)
    }

    suspend fun deleteAllTasks(){
        noteDao.deleteAllNotes()
    }

    fun searchDatabase(searchQuery: String) : Flow<List<NoteModel>> {
        return noteDao.searchDatabase(searchQuery = searchQuery)
    }

}