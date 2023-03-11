package com.dyor.scoop

//import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//import android.widget.Button



class MainActivity : AppCompatActivity() {

  private val TAG = "Demo"


  val db = Firebase.firestore

  private val signInLauncher = registerForActivityResult(
    FirebaseAuthUIActivityResultContract()
  ) { res ->
    this.onSignInResult(res)
  }

  val providers = arrayListOf(
    AuthUI.IdpConfig.EmailBuilder().build(),
    AuthUI.IdpConfig.PhoneBuilder().build(),
    AuthUI.IdpConfig.GoogleBuilder().build())

  // Create and launch sign-in intent
  val signInIntent = AuthUI.getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(providers)
    .build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    signInLauncher.launch(signInIntent)
    val signOutButton = findViewById<Button>(R.id.signOutButton) as Button
    signOutButton.setOnClickListener {
      //Toast.makeText(this, "You signed out boy!", Toast.LENGTH_LONG).show()
      SignOut()
    }
    val scoopsButton = findViewById<Button>(R.id.scoopsButton) as Button
    scoopsButton.setOnClickListener {
      //Toast.makeText(this, "You scooped boy!", Toast.LENGTH_LONG).show()
      val intent = Intent(this, ScoopList::class.java)
      startActivity(intent)
    }
    //CreateStuff()

  }

  private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    if (result.resultCode == RESULT_OK) {
      // Successfully signed in
      val user = FirebaseAuth.getInstance().currentUser
      val messageText = findViewById<TextView>(R.id.messageText) as TextView
      messageText.setText(user?.displayName)
      val imgPic = findViewById<ImageView>(R.id.imgPic)
      //val uri = "https://www.gstatic.com/devrel-devsite/prod/vb06f043a05fab8044a3ccc5b2a77caba73848fbe764e2f874782b493081fa838/cloud/images/cloud-logo.svg" as Uri
      //imgPic.setImageURI(uri)
      //imgPic.setImageResource("https://developer.android.com/static/codelabs/basic-android-kotlin-training-internet-images/img/e59b6e849e63ae2b_1920.png")

      val member = hashMapOf(
        "memberName" to user?.displayName,
        "memberUid" to user?.uid,
        "email" to user?.email,
        "phoneNumber" to user?.phoneNumber,
        "photoUrl" to user?.photoUrl,
      )

      //check to see if there is a user with this uid
      db.collection("users")
        .whereEqualTo("memberUid", user?.uid)
        .get()
        .addOnSuccessListener { task ->
          if (task.isEmpty) {
            db.collection("users")
              .add(member)
              .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
              }
              .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
              }
          }
        }




      // ...
    } else {
      // Sign in failed. If response is null the user canceled the
      // sign-in flow using the back button. Otherwise check
      // response.getError().getErrorCode() and handle the error.
      // ...
    }
  }
  private fun SignOut() {
    AuthUI.getInstance()
      .signOut(this)
      .addOnCompleteListener {
        signInLauncher.launch(signInIntent)
      }
  }
}