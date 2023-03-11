package com.dyor.scoop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
  var memberName: String? = null,
  var memberUid: String? = null,//uid for user


  ): Parcelable
