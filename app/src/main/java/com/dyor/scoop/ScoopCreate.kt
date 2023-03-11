package com.dyor.scoop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ScoopCreate : AppCompatActivity() {
  private lateinit var db: FirebaseFirestore
  private lateinit var fieldScoopNameEditText: EditText
  private lateinit var fieldDescriptionEditText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scoop_create)
    fieldScoopNameEditText = findViewById(R.id.edit_job_name)
    fieldDescriptionEditText = findViewById(R.id.edit_job_description)
    val saveScoopButton = findViewById<Button>(R.id.btnJobCreateSave) as Button
    saveScoopButton.setOnClickListener {

      createScoop()
    }


  }

  fun createScoop() {
    // lets try just creating a new scoop manually
    val buyer = Buyer("Matt Dyor", "1asdf", "an amazing buyer", Timestamp.now())
    val buyer1 = Buyer( "Matts Dyord", "2asdf", "an amazing buyer", Timestamp.now())
    val seller = Seller ("Matt Dyor", "3asdf", "an amazing seller", Timestamp.now())
    val seller1 = Seller("Matts Dyord", "4asdf", "an amazing ssdlld", Timestamp.now())

    var scoop = Scoop(
      null,
      fieldScoopNameEditText.text.toString(),
      FirebaseAuth.getInstance().currentUser?.uid,
      fieldDescriptionEditText.text.toString(),
      Timestamp.now(),
      buyers = null, //listOf(buyer, buyer1),
      sellers = null, //listOf(seller, seller1),
      userIds = listOf(FirebaseAuth.getInstance().currentUser?.uid.toString()),
      )
    val scoopNameText = fieldScoopNameEditText.text.toString()
    val descriptionText = fieldDescriptionEditText.text.toString()

    // val scoopHash = hashMapOf(
    //   "scoopName" to scoopNameText,
    //   "description" to descriptionText,
    // )

    val scoopHash = hashMapOf(
      "scoopName" to scoop.scoopName,
      "description" to scoop.description,
      "scoopManager" to scoop.scoopManager, //"scoopManager" to scoop.scoopManager,
      "description" to scoop.description,
      "timestamp" to scoop.timestamp,
      "buyers" to scoop.buyers,
      "sellers" to scoop.sellers,
      "userIds" to scoop.userIds,
    )
    db= FirebaseFirestore.getInstance()
    db.collection("scoops")
      .add(scoopHash)
      .addOnSuccessListener { documentReference ->
        Log.d("New", "DocumentSnapshot added with ID: ${documentReference.id}")
        Toast.makeText(this, "UYes!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, ScoopList::class.java)
        startActivity(intent)
      }
      .addOnFailureListener { e ->
        Log.w("New", "Error adding document", e)
        Toast.makeText(this, "NBoy!", Toast.LENGTH_LONG).show()
      }
  }
}

