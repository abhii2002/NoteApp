package com.example.noteapp.data.models

import android.graphics.Typeface
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.util.Constants


@Entity(tableName = Constants.DATABASE_TABLE)
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    val title: String,
    val noteDescription: String,
    val category: String,
    val date: String,
   // val profileImageFilePath : String,
)