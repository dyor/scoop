package com.dyor.scoop

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Buyer(
  var buyerName: String? = null,
  var buyerManager: String? = null,//uid for user
  var description: String? = null,
  var timestamp: Timestamp? = null,


  ): Parcelable
