package com.example

import androidx.recyclerview.widget.DiffUtil
import com.example.noteapp.data.models.NoteModel

class MyDiffUtil(
    private val oldList : List<NoteModel>,
    private val newList : List<NoteModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
       return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
         return when{
             oldList[oldItemPosition].id != newList[newItemPosition].id ->{
                 false
             }

             oldList[oldItemPosition].title != newList[newItemPosition].title ->{
                 false
             }

             oldList[oldItemPosition].noteDescription != newList[newItemPosition].noteDescription ->{
                 false
             }

             oldList[oldItemPosition].category != newList[newItemPosition].category ->{
                 false
             }

             oldList[oldItemPosition].date != newList[newItemPosition].date ->{
                 false
             }
             else -> {
                 true
             }

         }
    }
}