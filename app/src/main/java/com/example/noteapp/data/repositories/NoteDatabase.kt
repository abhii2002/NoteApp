package com.example.noteapp.data.repositories

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteapp.data.models.NoteModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


/*
Here we need to define all our tables that we have in our database
 */

@Database(entities = [NoteModel::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao


}