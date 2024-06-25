package com.example.notesapp

data class NoteItems(var title : String,var description : String,var noteId : String) {
constructor():this("","","")
}
