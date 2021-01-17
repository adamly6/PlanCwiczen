package com.example.aplikacjamobilna


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.ktx.auth

import android.widget.Toast

import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Gym Live"

        login_button_login.setOnClickListener {
           doLogin()
        }


        back_to_register.setOnClickListener {

            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun doLogin(){
        val email = login_edit.text.toString()
        val password = password_edit_login.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fields and try again!", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Login", "Próba logowania mail/hasło: $email/***")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = Firebase.auth.currentUser
                        Toast.makeText(baseContext, "Successful login", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MenuActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } else {
                        Toast.makeText(baseContext, "Email and password don't match", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}