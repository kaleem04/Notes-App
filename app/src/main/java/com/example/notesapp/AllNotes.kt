package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.ActivityAllNotesBinding
import com.example.notesapp.databinding.UpdateNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotes : AppCompatActivity(),RvAdapter.OnItemClickListener {
    private lateinit var binding: ActivityAllNotesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //initializing variables
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        recyclerView = binding.rvAllnotes
        recyclerView.layoutManager = LinearLayoutManager(this)

        getDataFromFirebase()

    }

    private fun getDataFromFirebase() {

        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val noteRef = databaseReference.child("users").child(user.uid).child("notes")
            noteRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItems>()

                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItems::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    val adapter = RvAdapter(noteList,this@AllNotes)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }


    }

    override fun deleteItem(noteId: String) {

        val currentUser = auth.currentUser

        currentUser?.let { task ->
            val noteRef =  databaseReference.child("users").child(task.uid).child("notes")
                noteRef.child(noteId).removeValue()
            }

    }

    override fun updateItem(noteId: String, currentTitle: String, currentDescription: String) {

        val binding = UpdateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(binding.root)
            .setTitle("\t\t\t\t\t\t\tUpdate Notes")
            .create()
        binding.etupdatetitle.setText(currentTitle)
        binding.etupdatedescription.setText(currentDescription)

        val positiveButton =  binding.btnsaveupdate
        val negativeButton = binding.btncancelupdate

        positiveButton.setOnClickListener{

            val newTitle = binding.etupdatetitle.text.toString()
            val newDescription = binding.etupdatedescription.text.toString()

            updateNote(noteId,newTitle,newDescription)
            dialog.dismiss()
        }
      negativeButton.setOnClickListener{
          dialog.dismiss()
      }



        dialog.show()

    }

    private fun updateNote(noteId: String, newTitle: String, newDescription: String) {

        val currentUser = auth.currentUser

        currentUser?.let {
                user ->

            val noteRef = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote = NoteItems(newTitle,newDescription,noteId)

            noteRef.child(noteId).setValue(updateNote)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Failed to update note: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


}