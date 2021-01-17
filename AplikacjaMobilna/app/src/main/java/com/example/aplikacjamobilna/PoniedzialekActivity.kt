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
import kotlinx.android.synthetic.main.activity_poniedzialek.*
import kotlinx.android.synthetic.main.poniedzialek_cwiczenia.view.*


class PoniedzialekActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poniedzialek)
        supportActionBar?.title = "Poniedziałek"

        add_button.setOnClickListener {
            add_button_click()
        }

        reset_poniedzialek.setOnClickListener {
            reset_poniedzialek()
        }

        fetchCwiczenia()

    }

    private fun fetchCwiczenia() {
        val uid=FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/poniedzialek")

        ref.addListenerForSingleValueEvent(object: ValueEventListener  {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    Log.d("NewMessage", it.toString() )

                    val nazwa_cwiczenie = it.getValue(Cwiczenie::class.java)
                    if (nazwa_cwiczenie != null) {
                        adapter.add(PoniedzialekItem(nazwa_cwiczenie))
                    }
                }
                cwiczenia.adapter = adapter
            }


        })

    }

    private fun reset_poniedzialek() {
        val uid=FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/poniedzialek/")
        ref.child("").removeValue()
        finish();
        startActivity(getIntent());

    }

    class PoniedzialekItem(val nazwa_cwiczenie: Cwiczenie): Item<GroupieViewHolder>(){

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

           viewHolder.itemView.tag = position
            viewHolder.itemView.ciwczenie_poniedzilek.text = nazwa_cwiczenie.nazwa
            if(nazwa_cwiczenie.zaliczone==1) {
                viewHolder.itemView.switch1.isChecked=true
            }
            viewHolder.itemView.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val uid=FirebaseAuth.getInstance().uid
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/poniedzialek/${position+1}")
                    ref.child("zaliczone").setValue(1)

                }
                else {
                    val uid=FirebaseAuth.getInstance().uid
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/cwiczenia/poniedzialek/${position+1}")
                    ref.child("zaliczone").setValue(0)
                }




            }



        }

        override fun getLayout(): Int {
            return R.layout.poniedzialek_cwiczenia
        }


    }

    private fun add_button_click() {

        val intent = Intent(this, PoniedzialekAddActivity::class.java)

        startActivity(intent)
    }



}



class Cwiczenie(val id: Int, val nazwa: String, val zaliczone: Int) {
    constructor() : this(-1, "", -1)
}