package com.example.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.RvItemsBinding

class RvAdapter(private val notes : List<NoteItems> ,private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RvAdapter.MyViewHolder>() {


    inner class MyViewHolder(var binding: RvItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NoteItems) {
            binding.txtRvTitle.text = data.title
            binding.txtRvDecription.text = data.description
        }


    }

    interface OnItemClickListener {
        fun deleteItem(noteId: String)
        fun updateItem(noteId: String ,currentTitle :String,currentDescription :String)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


       val data = notes[position]
        holder.bind(data)
        holder.binding.btndelete.setOnClickListener {

           clickListener.deleteItem(data.noteId)
        }

        holder.binding.btnupdate.setOnClickListener {
            clickListener.updateItem(data.noteId,data.title,data.description)

       }

    }



}