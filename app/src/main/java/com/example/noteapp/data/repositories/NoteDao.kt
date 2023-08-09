package com.example.noteapp.data.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteapp.data.models.NoteModel
import kotlinx.coroutines.flow.Flow

/*
 Inside our Dao(Data Access Object) we will need to define all our SQL queries which we are going to use
 with our database table
  */

@Dao
interface NoteDao {

    /*
We did not need to add the suspend function in these two queries because they are using Flow which
is a part of coroutines library and this flow is running asynchronously by default that's why we don't need
to add the suspend keyword to these queries
 */

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NoteModel>>

//    @Query("SELECT * FROM notes_table WHERE id=:taskId")
//    fun getSelectedTask(taskId: Int): Flow<NoteModel>

    @Query("SELECT * FROM notes_table WHERE category=:category")
    fun getSelectedCategory(category: String): Flow<List<NoteModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(noteModel : NoteModel) : Long

    /*
     But all the other queries which don't use Flow need to have  a suspend keyword because these functions
     will run in coroutines.
    */

    @Update
    suspend fun updateNote(noteModel: NoteModel)

    //delete a single task
    @Delete
    suspend fun deleteTask(noteModel: NoteModel)

    // deletes all tasks
    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()


    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery OR noteDescription LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<NoteModel>>






}