package com.dyor.scoop

import android.os.Parcelable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
class Job(
  var jobId: String? = null,
  var jobName: String? = null,
  var buyerId: String? = null,
  var sellerId: String? = null,
  var description: String? = null,
  var timestamp: Timestamp? = null,
  // var startTime: LocalTime = LocalTime.MIDNIGHT,
  var startTime2: String? = null,
  var startDate: String? = null, //LocalDate.now().toString(),
  var duration: Double = 0.0,
  var hourlyRate: Double = 0.0,
  var location: String? = null,
  var listedAt: Timestamp? = null,
  var acceptedAt: Timestamp? = null,
  var startedAt: Timestamp? = null,
  var finishedAt: Timestamp? = null,
  var paidAt: Timestamp? = null,
  var archivedAt: Timestamp? = null,
  var status: Status? = null,
  var isASeller: Boolean = false,
  var viewerUid: String? = null,


  ) : Parcelable

enum class Status {
  LISTED,
  ACCEPTED,
  STARTED,
  FINISHED,
  PAID,
  ARCHIVED
}

class SampleJobProvider: PreviewParameterProvider<Job> {
  override val values = sequenceOf(Job(
    "xyz123",
    "Sample Job Name",
    "buyerId",
    "sellerId",
    "This is the ultimate description of the this and that and the other",
    null,
    null,
    null,
    1.0,
    1.0,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    true,
    "xyz",
  ))
}