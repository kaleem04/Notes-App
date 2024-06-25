package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        binding.btnSave.setOnClickListener {

            saveNoteToFirebase()


        }
        binding.btnShownotes.setOnClickListener {

            startActivity(Intent(this,AllNotes::class.java))

        }




    }


    private fun saveNoteToFirebase(){

        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        if (title.isEmpty() && description.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }else{
            val currentUser = auth.currentUser
            currentUser?.let {
                user ->
                val notesRef = databaseReference.child("users").child(user.uid).child("notes")
                val newNoteKey = notesRef.push().key
                val note = NoteItems(title, description,newNoteKey ?: "" )

                if (newNoteKey != null) {
                    notesRef.child(newNoteKey).setValue(note)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
                            // Clear EditTexts after saving
                            binding.etTitle.text.clear()
                            binding.etDescription.text.clear()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error saving note: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } ?: run {
                // Handle case where no user is logged in
                Toast.makeText(this, "Please log in to save notes.", Toast.LENGTH_SHORT).show()
            }

            }
        }




    }



