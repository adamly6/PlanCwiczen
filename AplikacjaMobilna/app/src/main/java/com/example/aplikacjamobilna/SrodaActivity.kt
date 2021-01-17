package com.example.aplikacjamobilna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_sroda.*
import kotlinx.android.synthetic.main.sroda_cwiczenia.view.*

class SrodaActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sroda)
        supportActionBar?.title = "Sroda"

        add_button.setOnClickListener {
            add_button_click()
        }

        reset_sroda.setOnClickListener {
            reset_sroda()
        }

        fetchCwiczenia()

    }

    private fun fetchCwiczenia() {
        val uid= FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString() )

                    val nazwa_cwiczenie = it.getValue(Cwiczenie::class.java)
                    if (nazwa_cwiczenie != null) {
                        adapter.add(SrodaItem(nazwa_cwiczenie))
                    }
                }
                cwiczenia.adapter = adapter
            }


        })

    }

    private fun reset_sroda() {
        val uid= FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda/")
        ref.child("").removeValue()
        finish();
        startActivity(getIntent());

    }

    class SrodaItem(val nazwa_cwiczenie: Cwiczenie): Item<GroupieViewHolder>(){

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            viewHolder.itemView.tag = position
            viewHolder.itemView.cwiczenie_sroda.text = nazwa_cwiczenie.nazwa
            if(nazwa_cwiczenie.zaliczone==1) {
                viewHolder.itemView.switch1.isChecked=true
            }
            viewHolder.itemView.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val uid= FirebaseAuth.getInstance().uid
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda/${position+1}")
                    ref.child("zaliczone").setValue(1)

                }
                else {
                    val uid= FirebaseAuth.getInstance().uid
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/sroda/${position+1}")
                    ref.child("zaliczone").setValue(0)
                }




            }



        }

        override fun getLayout(): Int {
            return R.layout.sroda_cwiczenia
        }


    }

    private fun add_button_click() {

        val intent = Intent(this, SrodaAddActivity::class.java)

        startActivity(intent)
    }



}
