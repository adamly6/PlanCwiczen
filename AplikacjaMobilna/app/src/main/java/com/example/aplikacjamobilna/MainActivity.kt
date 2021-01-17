package com.example.aplikacjamobilna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseReference



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Gym Live"




        register_button_register.setOnClickListener {

            performRegister()

        }

        alredy_have_acount.setOnClickListener {
            Log.d( " MainActivity",  "Try to show login activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        Log.d( " MainActivity", "Email is:$email")
        Log.d( " MainActivity",  "Password: $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Wpisz hasło i mail", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main", "Sukces: ${it.result?.user?.uid}")

                    saveUserToDatabase()


                }
                .addOnFailureListener {
                    Log.d("Main", "Błąd przy dodawaniu użytkownika: ${it.message}")
                    Toast.makeText(this, "Błąd przy dodawaniu użytkownika: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, username_edittext_register.text.toString())
        ref.setValue(user)
                .addOnSuccessListener {
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }





    }
}

class User(val uid: String, val username: String)