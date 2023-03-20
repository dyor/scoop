package com.dyor.scoop
import android.os.Parcelable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize


//data class Scoop(var scoopId: String? = null, var scoopName: String? = null)
@Parcelize
data class Scoop(
  var scoopId: String? = null,
  var scoopName: String? = null,
  var scoopManager: String? = null,//uid for user
  var description: String? = null,
  var timestamp: Timestamp? = null,
  var buyers: List<Buyer>? = null,
  var sellers: List<Seller>? = null,
  var jobs: List<Job>? = null,
  var userIds: List<String>? = null,

  ) : Parcelable

class SampleScoopProvider: PreviewParameterProvider<Scoop> {
  override val values = sequenceOf(Scoop(
    "xyz123",
    "Sample Scoop Name",
    "user1",
    "This is my big awesome Scoop"
  ))
}