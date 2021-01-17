package com.example.aplikacjamobilna

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_poniedzialek.*
import kotlinx.android.synthetic.main.all_cwiczenia.view.*
import kotlinx.android.synthetic.main.poniedzialek_add.*

class AddActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.title = "Dodaj ćwiczenie do puli"


        fetchAdd()

        add_cwicznie_button.setOnClickListener {
            addExe()
        }


    }

    private fun addExe() {
        val dodawanie_cwiczenie = cwicznie_edit.text.toString()

        if(dodawanie_cwiczenie.isEmpty()){
            Toast.makeText(this, "Please fill all fields and try again!", Toast.LENGTH_SHORT).show()
            return
        }

        val uid= FirebaseAuth.getInstance().uid
        val ref1 = FirebaseDatabase.getInstance().getReference("/cwiczenia")
        var licznik = 1
        ref1.addListenerForSingleValueEvent(object: ValueEventListener  {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {



                snapshot.children.forEach {
                    Log.d("NewMessage", "$licznik" )
                    licznik++

                }

                    val ref = FirebaseDatabase.getInstance().getReference("/cwiczenia/${licznik}")

                   ref.child("id").setValue(licznik)
                     ref.child("nazwa").setValue(dodawanie_cwiczenie)
                finish();
                startActivity(getIntent());


            }


        })

    }

    private fun fetchAdd() {

        val ref = FirebaseDatabase.getInstance().getReference("/cwiczenia")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString() )

                    val nazwa_cwiczeniee = it.getValue(CwiczenieAdd::class.java)

                    if (nazwa_cwiczeniee != null) {
                        adapter.add(AddItem2(nazwa_cwiczeniee))
                    }
                }
                add_all_cwiczenia.adapter = adapter
            }


        })

    }

    class AddItem2(val nazwa_cwiczeniee: CwiczenieAdd): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.cwiczenie_add.text = nazwa_cwiczeniee.nazwa

        }

        override fun getLayout(): Int {
            return R.layout.all_cwiczenia
        }
    }

}
