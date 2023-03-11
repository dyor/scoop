package com.dyor.scoop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class JobCreate : AppCompatActivity() {
  private lateinit var db: FirebaseFirestore
  private lateinit var fieldJobNameEditText: EditText
  private lateinit var fieldDescriptionEditText: EditText
  private lateinit var fieldStartTimeText: EditText
  private lateinit var fieldStartDateText: EditText
  private lateinit var fieldDurationText: EditText
  private lateinit var fieldHourlyRateText: EditText
  private lateinit var fieldLocationText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_job_create)
    var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)

    fieldJobNameEditText = findViewById(R.id.edit_job_name)
    fieldDescriptionEditText = findViewById(R.id.edit_job_description)
    fieldStartTimeText = findViewById(R.id.edit_start_time)
    fieldStartDateText = findViewById(R.id.edit_start_date)
    fieldDurationText = findViewById(R.id.edit_duration)
    fieldHourlyRateText = findViewById(R.id.edit_hourly_rate)
    fieldLocationText = findViewById(R.id.edit_location)
    val saveJobButton = findViewById<Button>(R.id.btnJobCreateSave) as Button
    saveJobButton.setOnClickListener {
      if (scoop != null) {
        createJob(scoop)
      }
    }
  }
  var fdate: DateTimeFormatter = DateTimeFormatterBuilder().parseCaseInsensitive()
    .append(DateTimeFormatter.ofPattern("yyyy/MM/DD")).toFormatter()
  var fhour: DateTimeFormatter = DateTimeFormatterBuilder().parseCaseInsensitive()
    .append(DateTimeFormatter.ofPattern("hh:mm")).toFormatter()

  fun createJob(scoop: Scoop) {
//need to add hourly rate, start date, and duration to activity and then here.
    var job = Job(
      jobId = null,
      jobName = fieldJobNameEditText.text.toString(),
      buyerId = FirebaseAuth.getInstance().currentUser?.uid,
      sellerId = null,
      description = fieldDescriptionEditText.text.toString(),
      timestamp = Timestamp.now(),
      startTime2 = fieldStartTimeText.text.toString(), //LocalTime.parse(fieldStartTimeText.text.toString().format(fhour)),
      startDate = fieldStartDateText.text.toString(),
      duration = fieldDurationText.text.toString().toDouble(),
      hourlyRate = fieldHourlyRateText.text.toString().toDouble(),
      location = fieldLocationText.text.toString(),
      listedAt = Timestamp.now(),
      null,
      null,
      status = Status.LISTED
      )
    // val scoopNameText = fieldScoopNameEditText.text.toString()
    // val descriptionText = fieldDescriptionEditText.text.toString()

    val jobHash = hashMapOf(
      "jobName" to job.jobName,
      "buyerId" to job.buyerId,
      "description" to job.description,
      "timestamp" to job.timestamp,
      "listedAt" to job.listedAt,
      "status" to job.status,
    )

    var newJobs: ArrayList<Job> = arrayListOf()
    if (scoop.jobs != null) {
      for (thisJob in scoop.jobs!!) {
        newJobs.add(thisJob)
      }
    }
    newJobs.add(job)
    scoop.jobs = newJobs

    db= FirebaseFirestore.getInstance()

    db.collection("scoops").document(scoop.scoopId!!)
      .set(scoop)
      .addOnSuccessListener { documentReference ->
        Toast.makeText(this, "I have added job!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, ScoopDetail::class.java)
        intent.putExtra("scoop", scoop)
        startActivity(intent)
      }
      .addOnFailureListener { e ->
        Log.w("New", "Error adding document", e)
      }
  }
}