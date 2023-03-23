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
import com.google.firebase.FirebaseApp


class JobDetail : ComponentActivity() {

  private lateinit var db: FirebaseFirestore
  private lateinit var scoop: Scoop
  private lateinit var job: Job

  //control the workflow
  // private lateinit var itemAccept: Button
  // private lateinit var itemStart: Button
  // private lateinit var itemFinish: Button
  // private lateinit var itemPay: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //setContentView(R.layout.activity_job_detail)
    // val itemIdTextView = findViewById<TextView>(R.id.job_name)
    // val itemIdTextDescription = findViewById<TextView>(R.id.job_description)


    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // if (job != null && scoop != null) {
    //   itemIdTextView.text = job.jobName
    //   itemIdTextDescription.text = job.description
    //val formattedDate = format.format(Date(job.listedAt!!.seconds * 1000))
    //   txtListed.text = "Listed At: $formattedDate"
    //   //first we set all to basic
    //   //handleWorkflow(job, scoop)
    // }
    setContent {
      ComposeTheme {
        Surface {
          var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
          var job = intent.getParcelableExtra("job", Job::class.java)
          val user = FirebaseAuth.getInstance().currentUser
          val sellerIds = scoop?.sellers?.map { seller: Seller -> seller.sellerManager }
          val isASeller = sellerIds?.contains(user?.uid) == true
          if (job != null && scoop != null) {
            job.isASeller = isASeller
            job.viewerUid = user!!.uid
            ParentLayout(job, scoop)
          }
        }
      }
    }
  }

  @Composable
  fun ParentLayout(job: Job, scoop: Scoop) {
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
        colors = ButtonDefaults
          .buttonColors(
            contentColor = lightColorScheme().primary,
            )

        //colors = ButtonDefaults.buttonColors(contentColor = Color.DarkGray)
      ) {
        Text(text = "Back", color = Color.White)
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
        //revert job to state in firestore
        val isASeller = job.isASeller
        val viewerUid = job.viewerUid
        job.isASeller = false
        job.viewerUid = null
        //prepare for workflow updates
        var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
        db = FirebaseFirestore.getInstance()
        val jobRef = db.collection("scoops").document(scoop?.scoopId!!)


        //workflow buttons

        var acceptedColor = lightColorScheme().secondaryContainer
        var acceptedEnabled = false
        var startedColor = lightColorScheme().secondaryContainer
        var startedEnabled = false
        var finishedColor = lightColorScheme().secondaryContainer
        var finishedEnabled = false
        var paidColor = lightColorScheme().secondaryContainer
        var paidEnabled = false
        val isSeller = job?.sellerId == viewerUid
        val isBuyer = job?.buyerId == viewerUid
        if (job?.acceptedAt == null) {
          acceptedColor = lightColorScheme().primaryContainer
          if (isASeller) {
            acceptedEnabled = true
          }
        } else if (job?.startedAt == null) {
          startedColor = lightColorScheme().primaryContainer
          if (isBuyer) {
            startedEnabled = true
          }
        } else if (job.finishedAt == null) {
          finishedColor = lightColorScheme().primaryContainer
          if (isSeller) {
            finishedEnabled = true
          }
        } else if (job.paidAt == null) {
          paidColor = lightColorScheme().primaryContainer
          if (isBuyer) {
            paidEnabled = true
          }
        }
        Button(
          onClick = {
            jobRef.update("jobs", FieldValue.arrayRemove(job))
            job?.acceptedAt = Timestamp.now()
            job?.sellerId = viewerUid
            jobRef.update("jobs", FieldValue.arrayUnion(job))
            val intent = Intent(context, JobDetail::class.java)
            intent.putExtra("scoop", scoop)
            intent.putExtra("job", job)
            startActivity(intent)
          },
          enabled = acceptedEnabled,
          colors = ButtonDefaults.buttonColors(contentColor = acceptedColor)
        ) {
          Text(text = "Seller: Accept", color = Color.White)
        }
        Button(
          onClick = {
            jobRef.update("jobs", FieldValue.arrayRemove(job))
            job?.startedAt = Timestamp.now()
            jobRef.update("jobs", FieldValue.arrayUnion(job))
            val intent = Intent(context, JobDetail::class.java)
            intent.putExtra("scoop", scoop)
            intent.putExtra("job", job)
            startActivity(intent)
          },
          enabled = startedEnabled,
          colors = ButtonDefaults.buttonColors(contentColor = startedColor)
        ) {
          Text(text = "Seller: Start", color = Color.White)
        }
        Button(
          onClick = {
            jobRef.update("jobs", FieldValue.arrayRemove(job))
            job?.finishedAt = Timestamp.now()
            jobRef.update("jobs", FieldValue.arrayUnion(job))
            val intent = Intent(context, JobDetail::class.java)
            intent.putExtra("scoop", scoop)
            intent.putExtra("job", job)
            startActivity(intent)
          },
          enabled = finishedEnabled,
          colors = ButtonDefaults.buttonColors(contentColor = finishedColor)
        ) {
          Text(text = "Seller: Complete", color = Color.White)
        }
        Button(
          onClick = {
            jobRef.update("jobs", FieldValue.arrayRemove(job))
            job?.paidAt = Timestamp.now()
            jobRef.update("jobs", FieldValue.arrayUnion(job))
            val intent = Intent(context, JobDetail::class.java)
            intent.putExtra("scoop", scoop)
            intent.putExtra("job", job)
            startActivity(intent)
          },
          enabled = paidEnabled,
          colors = ButtonDefaults.buttonColors(contentColor = paidColor)
        ) {
          Text(text = "Buyer: Paid", color = Color.White)
        }
      }
    }
  }
}


