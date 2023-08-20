package com.example.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.models.NoteModel


/*
Here we need to define all our tables that we have in our database
 */

@Database(entities = [NoteModel::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao


}