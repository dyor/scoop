package com.dyor.scoop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.getField

class ScoopList : AppCompatActivity() {
  private lateinit var recyclerView: RecyclerView
  private lateinit var scoopArrayList: ArrayList<Scoop>
  private lateinit var scoopAdapter: ScoopAdapter
  private lateinit var db: FirebaseFirestore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scoop_list)

    recyclerView = findViewById(R.id.scoopRecyclerView)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.setHasFixedSize(true)

    scoopArrayList = arrayListOf()
    scoopAdapter = ScoopAdapter(scoopArrayList) {
      Log.d("Activity", "Clicked on item ${it.scoopName}")
      val intent = Intent(this, ScoopDetail::class.java)
      //intent.putExtra("scoopName", it.scoopName)
      //intent.putExtra("scoopId", it.scoopId)
      intent.putExtra("scoop", it)
      startActivity(intent)
    }
    recyclerView.adapter = scoopAdapter
    val newScoopButton = findViewById<Button>(R.id.btnNewScoop) as Button
    newScoopButton.setOnClickListener {
      Toast.makeText(this, "You scooped boy!!!", Toast.LENGTH_LONG).show()
      val intent = Intent(this, ScoopCreate::class.java)
      startActivity(intent)
    }
    eventChangeListener()
  }

  private fun eventChangeListener() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    db= FirebaseFirestore.getInstance()
    db.collection("scoops")
      .whereArrayContains("userIds", uid)
      .orderBy("timestamp", Query.Direction.DESCENDING)
      .addSnapshotListener { value, error ->
        if (error!= null) {
          Log.e("Firestore Error", error.message.toString() )
          return@addSnapshotListener
        }
        for(dc : DocumentChange in value?.documentChanges!!){
          //if (dc.type == DocumentChange.Type.ADDED || dc.type == DocumentChange.Type.MODIFIED){
            var scoop = dc.document.toObject(Scoop::class.java)
            scoop.scoopId = dc.document.id
            scoop.scoopName = dc.document.getField("scoopName")
            scoopArrayList.add(scoop)
          //}
        }
        scoopAdapter.notifyDataSetChanged()
      }
  }
}