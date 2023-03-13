package com.dyor.scoop


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.w3c.dom.Text


class JobDetail : AppCompatActivity() {

  private lateinit var db: FirebaseFirestore
  private lateinit var scoop: Scoop
  //control the workflow
  private lateinit var itemAccept: Button
  private lateinit var itemStart: Button
  private lateinit var itemFinish: Button
  private lateinit var itemPay: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_job_detail)
    val itemIdTextView = findViewById<TextView>(R.id.job_name)
    val itemIdTextDescription = findViewById<TextView>(R.id.job_description)


    var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
    var job = intent.getParcelableExtra("job", Job::class.java)
    val txtListed = findViewById<TextView>(R.id.txtListed)
    val itemBackJob = findViewById<Button>(R.id.btnBackJob)
    itemBackJob.setOnClickListener {
      val intent = Intent(this, ScoopDetail::class.java)
      intent.putExtra("scoop", scoop)
      startActivity(intent)
    }
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    if (job != null && scoop != null) {
      itemIdTextView.text = job.jobName
      itemIdTextDescription.text = job.description
      val formattedDate = format.format(Date(job.listedAt!!.seconds*1000))
      txtListed.text = "Listed At: $formattedDate"
      //first we set all to basic
      handleWorkflow(job, scoop)
    }
  }
  private fun handleWorkflow(job: Job, scoop: Scoop) {
    itemAccept = findViewById(R.id.btnAccept)
    itemStart = findViewById(R.id.btnStart)
    itemFinish = findViewById(R.id.btnFinish)
    itemPay = findViewById(R.id.btnPay)
    itemAccept.setBackgroundColor(Color.GRAY)
    itemStart.setBackgroundColor(Color.GRAY)
    itemFinish.setBackgroundColor(Color.GRAY)
    itemPay.setBackgroundColor(Color.GRAY)


    if (job.acceptedAt == null) {
      itemAccept.isEnabled = true
      itemAccept.setBackgroundColor(Color.CYAN)
      //itemAccept.visibility = View.VISIBLE
      itemAccept.setOnClickListener {
        db= FirebaseFirestore.getInstance()
        val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
        jobRef.update("jobs", FieldValue.arrayRemove(job))
        job.acceptedAt = Timestamp.now()
        jobRef.update("jobs", FieldValue.arrayUnion(job))
        handleWorkflow(job, scoop)
      }
    }
    else if (job.startedAt==null)
    {
      itemStart.isEnabled = true
      itemStart.setBackgroundColor(Color.CYAN)
      //itemAccept.visibility = View.VISIBLE
      itemStart.setOnClickListener {
        db= FirebaseFirestore.getInstance()
        val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
        jobRef.update("jobs", FieldValue.arrayRemove(job))
        job.startedAt = Timestamp.now()
        jobRef.update("jobs", FieldValue.arrayUnion(job))
        handleWorkflow(job, scoop)
      }
    }
    else if (job.finishedAt==null)
    {
      itemFinish.isEnabled = true
      itemFinish.setBackgroundColor(Color.CYAN)
      //itemAccept.visibility = View.VISIBLE
      itemFinish.setOnClickListener {
        db= FirebaseFirestore.getInstance()
        val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
        jobRef.update("jobs", FieldValue.arrayRemove(job))
        job.finishedAt = Timestamp.now()
        jobRef.update("jobs", FieldValue.arrayUnion(job))
        handleWorkflow(job, scoop)
      }
    }
    else if (job.paidAt==null)
    {
      itemPay.isEnabled = true
      itemPay.setBackgroundColor(Color.CYAN)
      //itemAccept.visibility = View.VISIBLE
      itemPay.setOnClickListener {
        db= FirebaseFirestore.getInstance()
        val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
        jobRef.update("jobs", FieldValue.arrayRemove(job))
        job.paidAt = Timestamp.now()
        jobRef.update("jobs", FieldValue.arrayUnion(job))
        handleWorkflow(job, scoop)
      }
    }
  }
}

