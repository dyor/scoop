package com.dyor.scoop


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class JobDetail : AppCompatActivity() {

  private lateinit var db: FirebaseFirestore
  var scoop = Scoop()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_job_detail)
    val itemIdTextView = findViewById<TextView>(R.id.job_name)
    val itemIdTextDescription = findViewById<TextView>(R.id.job_description)
    val itemBackJob = findViewById<Button>(R.id.btnBackJob)

    var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
    var job = intent.getParcelableExtra("job", Job::class.java)
    itemBackJob.setOnClickListener {
      val intent = Intent(this, ScoopDetail::class.java)
      intent.putExtra("scoop", scoop)
      startActivity(intent)
    }
    itemIdTextView.text = job!!.jobName
    itemIdTextDescription.text = job!!.description
  }






}

