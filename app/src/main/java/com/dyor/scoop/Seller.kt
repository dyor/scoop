package com.dyor.scoop

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seller(
  var sellerName: String? = null,
  var sellerManager: String? = null,//uid for user
  var description: String? = null,
  var timestamp: Timestamp? = null,


  ): Parcelable
