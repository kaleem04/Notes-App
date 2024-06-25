package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.txtAlready.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        auth = FirebaseAuth.getInstance()


        binding.btnRegister.setOnClickListener {

            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()



            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){

                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()

            }else if(password != confirmPassword) {

                Toast.makeText(this, "Passwords Must Be Same", Toast.LENGTH_SHORT).show()
            }else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration SuccessFull", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed : ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }



            }
            }




        }
    }
}