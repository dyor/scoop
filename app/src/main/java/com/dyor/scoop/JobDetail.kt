package com.dyor.scoop


import android.content.Intent
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.KeyEventDispatcher.Component
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dyor.scoop.theme.ComposeTheme


class JobDetail : ComponentActivity() {

  private lateinit var db: FirebaseFirestore
  private lateinit var scoop: Scoop
  //control the workflow
  // private lateinit var itemAccept: Button
  // private lateinit var itemStart: Button
  // private lateinit var itemFinish: Button
  // private lateinit var itemPay: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_job_detail)
    val itemIdTextView = findViewById<TextView>(R.id.job_name)
    val itemIdTextDescription = findViewById<TextView>(R.id.job_description)


    var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
    var job = intent.getParcelableExtra("job", Job::class.java)
    val txtListed = findViewById<TextView>(R.id.txtListed)
    // val itemBackJob = findViewById<Button>(R.id.btnBackJob)
    // itemBackJob.setOnClickListener {
    //   val intent = Intent(this, ScoopDetail::class.java)
    //   intent.putExtra("scoop", scoop)
    //   startActivity(intent)
    // }
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    if (job != null && scoop != null) {
      itemIdTextView.text = job.jobName
      itemIdTextDescription.text = job.description
      val formattedDate = format.format(Date(job.listedAt!!.seconds * 1000))
      txtListed.text = "Listed At: $formattedDate"
      //first we set all to basic
      //handleWorkflow(job, scoop)
    }
    setContent {
      ComposeTheme {
        Surface {
          if (job != null && scoop != null) {
            ParentLayout(job, scoop)
          }
        }
      }
    }
  }

  @Composable
  fun ParentLayout(job: Job, scoop: Scoop){
    Column {
      BackButton(scoop)
      JobDetails(job)
    }
  }

  @Preview
  @Composable
  fun BackButton(
    @PreviewParameter(SampleScoopProvider::class) scoop: Scoop,
  ) {
    val context = LocalContext.current
    Column(
      modifier = Modifier
        .padding(20.dp)
    ) {
      Button(
        onClick = {
          val intent = Intent(context, ScoopDetail::class.java)
          intent.putExtra("scoop", scoop)
          startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(contentColor = darkColorScheme().primary)
        //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
      ) {
        Text(text = "Back")
      }

    }
  }

  @Preview
  @Composable
  fun JobDetails(
    @PreviewParameter(SampleJobProvider::class) job: Job
  ) {
    val context = LocalContext.current
    Surface {
      Column(
        modifier = Modifier
          .padding(20.dp)
      ) {
        job.jobName?.let {
          Text(
            it,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
              fontSize = 24.sp
            )
          )
        }
        job.description?.let {
          Text(
            it,
            style = TextStyle(
              fontSize = 16.sp
            )
          )
        }
        //workflow buttons
        Button(
          onClick = {
            val intent = Intent(context, ScoopDetail::class.java)
            intent.putExtra("scoop", scoop)
            startActivity(intent)
          },
          //colors = ButtonDefaults.buttonColors(contentColor = darkColorScheme().primary)
          //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
        ) {
          Text(text = "Seller: Accept")
        }
        Button(
          onClick = {
            val intent = Intent(context, ScoopDetail::class.java)
            intent.putExtra("scoop", scoop)
            startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(containerColor = darkColorScheme().secondaryContainer)
          //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
        ) {
          Text(text = "Seller: Start")
        }
        Button(
          onClick = {
            val intent = Intent(context, ScoopDetail::class.java)
            intent.putExtra("scoop", scoop)
            startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(containerColor = darkColorScheme().secondaryContainer)
          //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
        ) {
          Text(text = "Seller: Complete")
        }
        Button(
          onClick = {
            val intent = Intent(context, ScoopDetail::class.java)
            intent.putExtra("scoop", scoop)
            startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(contentColor = lightColorScheme().primary, containerColor = lightColorScheme().primaryContainer)
          //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
        ) {
          Text(text = "Buyer: Pay")
        }
      }
    }
  }


  // private fun handleWorkflow(job: Job, scoop: Scoop) {
  //   itemAccept = findViewById(R.id.btnAccept)
  //   itemStart = findViewById(R.id.btnStart)
  //   itemFinish = findViewById(R.id.btnFinish)
  //   itemPay = findViewById(R.id.btnPay)
  //   itemAccept.setBackgroundColor(Color.GRAY)
  //   itemStart.setBackgroundColor(Color.GRAY)
  //   itemFinish.setBackgroundColor(Color.GRAY)
  //   itemPay.setBackgroundColor(Color.GRAY)
  //   val sellerIds = scoop.sellers?.map { seller: Seller -> seller.sellerManager}
  //   val user = FirebaseAuth.getInstance().currentUser
  //   val isASeller = sellerIds?.contains(user?.uid)==true
  //   val isSeller = job.sellerId == user?.uid
  //   val isBuyer = job.buyerId == user?.uid
  //
  //   if (job.acceptedAt == null) {
  //     //itemAccept.visibility = View.VISIBLE
  //     itemAccept.setBackgroundColor(Color.CYAN)
  //     if (isASeller) {
  //       itemAccept.isEnabled = true
  //       itemAccept.setOnClickListener {
  //         db = FirebaseFirestore.getInstance()
  //         val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
  //         jobRef.update("jobs", FieldValue.arrayRemove(job))
  //         job.acceptedAt = Timestamp.now()
  //         job.sellerId = user?.uid
  //         jobRef.update("jobs", FieldValue.arrayUnion(job))
  //         handleWorkflow(job, scoop)
  //       }
  //     }
  //     else {
  //         Toast.makeText(this, "Waiting on a Seller to Accept", Toast.LENGTH_LONG).show()
  //       }
  //     }
  //   else if (job.startedAt==null) {
  //     itemStart.setBackgroundColor(Color.CYAN)
  //     if (isBuyer) {
  //       itemStart.isEnabled = true
  //       //itemAccept.visibility = View.VISIBLE
  //       itemStart.setOnClickListener {
  //         db = FirebaseFirestore.getInstance()
  //         val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
  //         jobRef.update("jobs", FieldValue.arrayRemove(job))
  //         job.startedAt = Timestamp.now()
  //         jobRef.update("jobs", FieldValue.arrayUnion(job))
  //         handleWorkflow(job, scoop)
  //       }
  //     }
  //     else{
  //       Toast.makeText(this, "Waiting on the Seller to Start", Toast.LENGTH_LONG).show()
  //     }
  //   }
  //   else if (job.finishedAt==null)
  //   {
  //     itemFinish.setBackgroundColor(Color.CYAN)
  //     if (isSeller) {
  //       itemFinish.isEnabled = true
  //       //itemAccept.visibility = View.VISIBLE
  //       itemFinish.setOnClickListener {
  //         db = FirebaseFirestore.getInstance()
  //         val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
  //         jobRef.update("jobs", FieldValue.arrayRemove(job))
  //         job.finishedAt = Timestamp.now()
  //         jobRef.update("jobs", FieldValue.arrayUnion(job))
  //         handleWorkflow(job, scoop)
  //       }
  //     }
  //     else{
  //       Toast.makeText(this, "Waiting on the Seller to Finish", Toast.LENGTH_LONG).show()
  //     }
  //   }
  //   else if (job.paidAt==null) {
  //     itemPay.setBackgroundColor(Color.CYAN)
  //     if (isBuyer) {
  //       itemPay.isEnabled = true
  //       //itemAccept.visibility = View.VISIBLE
  //       itemPay.setOnClickListener {
  //         db = FirebaseFirestore.getInstance()
  //         val jobRef = db.collection("scoops").document(scoop?.scoopId!!)
  //         jobRef.update("jobs", FieldValue.arrayRemove(job))
  //         job.paidAt = Timestamp.now()
  //         jobRef.update("jobs", FieldValue.arrayUnion(job))
  //         handleWorkflow(job, scoop)
  //       }
  //
  //     }
  //     else {
  //       Toast.makeText(this, "Waiting on the Buyer to Pay", Toast.LENGTH_LONG).show()
  //     }
  //   }
  //
  //
  // }
}

