package com.example.aplikacjamobilna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.title = "Gym Live"

        poniedzialek_button.setOnClickListener {
            poniedzialekSelected()
        }
        wtorek_button.setOnClickListener {
            wtorekSelected()
        }

        sroda_button.setOnClickListener {
            srodaSelected()
        }

        czwartek_button.setOnClickListener {
            czwartekSelected()
        }

        piatek_button.setOnClickListener {
            piatekSelected()
        }

        sobota_button.setOnClickListener {
            sobotaSelected()
        }

        niedziela_button.setOnClickListener {
            niedzielaSelected()
        }

        veryfiUserIsLoggedIn()
        }

    private fun veryfiUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_add_exe -> {

                val intent = Intent(this, AddActivity::class.java)

                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    private fun poniedzialekSelected() {

            val intent = Intent(this, PoniedzialekActivity::class.java)

            startActivity(intent)
    }

    private fun wtorekSelected() {

        val intent = Intent(this, WtorekActivity::class.java)

        startActivity(intent)
    }

    private fun srodaSelected() {

        val intent = Intent(this, SrodaActivity::class.java)

        startActivity(intent)
    }

    private fun czwartekSelected() {

        val intent = Intent(this, CzwartekActivity::class.java)

        startActivity(intent)
    }
    private fun piatekSelected() {

        val intent = Intent(this, PiatekActivity::class.java)

        startActivity(intent)
    }
    private fun sobotaSelected() {

        val intent = Intent(this, SobotaActivity::class.java)

        startActivity(intent)
    }
    private fun niedzielaSelected() {

        val intent = Intent(this, NiedzielaActivity::class.java)

        startActivity(intent)
    }
}