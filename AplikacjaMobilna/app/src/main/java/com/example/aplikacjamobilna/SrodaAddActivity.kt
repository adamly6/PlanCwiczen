package com.example.aplikacjamobilna

import android.content.Context
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
import kotlinx.android.synthetic.main.all_cwiczenia.view.*
import kotlinx.android.synthetic.main.sroda_add.*

class SrodaAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sroda_add)
        supportActionBar?.title = "Wybierz ćwiczenie"


        fetchAdd(this.applicationContext)




    }

    private fun fetchAdd(applicationContext: Context) {

        val ref = FirebaseDatabase.getInstance().getReference("/cwiczenia")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString() )

                    val nazwa_cwiczenie = it.getValue(CwiczenieAdd::class.java)

                    if (nazwa_cwiczenie != null) {
                        adapter.add(AddItem(nazwa_cwiczenie, applicationContext))
                    }
                }
                all_cwiczenia.adapter = adapter
            }


        })

    }

    class AddItem(val nazwa_cwiczenie: CwiczenieAdd, val applicationContext: Context): Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.cwiczenie_add.text = nazwa_cwiczenie.nazwa
            viewHolder.itemView.cwiczenie_add.setOnClickListener {
                Toast.makeText(applicationContext, "Dodano ćwiczenie: ${nazwa_cwiczenie.nazwa}", Toast.LENGTH_SHORT).show()
                val uid= FirebaseAuth.getInstance().uid
                val ref1 = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda")
                var licznik = 1
                ref1.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {



                        snapshot.children.forEach {
                            Log.d("NewMessage", "$licznik" )
                            licznik++

                        }

                        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda/${nazwa_cwiczenie.id}")

                        ref.child("id").setValue(nazwa_cwiczenie.id)
                        ref.child("nazwa").setValue(nazwa_cwiczenie.nazwa)
                        ref.child("zaliczone").setValue(0)


                    }


                })



            }


        }

        override fun getLayout(): Int {
            return R.layout.all_cwiczenia

        }
    }

}