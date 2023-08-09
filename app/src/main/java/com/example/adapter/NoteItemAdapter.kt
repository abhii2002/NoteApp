package com.example.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.MyDiffUtil
import com.example.noteapp.R
import com.example.noteapp.activities.ResultActivity
import com.example.noteapp.data.models.NoteModel
import com.google.gson.Gson




class NoteItemAdapter(): RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {

  private var oldNote = emptyList<NoteModel>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val noteTitle : TextView = itemView.findViewById(R.id.tv_noteTitle)
        val date : TextView = itemView.findViewById(R.id.tv_date)
        val  noteDescription : TextView = itemView.findViewById(R.id.tv_noteDescription)
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_note_item, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // using json to pass an object through intent .
        val gson = Gson()
        val notes = oldNote[position]

        var myJson = gson.toJson(notes)


        holder.noteTitle.text = notes.title
        holder.noteDescription.text = notes.noteDescription
        holder.date.text = notes.date



        holder.view.setOnClickListener {
            val intent = Intent(it.context, ResultActivity::class.java)
            intent.putExtra("id", notes.id)
            intent.putExtra("note_title", notes.title)
            intent.putExtra("note_description", notes.noteDescription)
            intent.putExtra("note_model", myJson)
            intent.putExtra("note_category", notes.category)
            intent.putExtra("note_date", notes.date)


            holder.view.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return oldNote.size
    }


    fun setData(newNote: List<NoteModel>){
        val diffUtil = MyDiffUtil(oldNote, newNote)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldNote = newNote
        diffResults.dispatchUpdatesTo(this)


    }







}