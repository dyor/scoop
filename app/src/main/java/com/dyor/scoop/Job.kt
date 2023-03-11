package com.dyor.scoop

import android.os.Parcelable
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


  ) : Parcelable

enum class Status {
  LISTED,
  ACCEPTED,
  STARTED,
  FINISHED,
  PAID,
  ARCHIVED
}