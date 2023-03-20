package com.dyor.scoop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class JobViewModel: ViewModel() {
  private val _state = MutableStateFlow(JobState())
  val state = _state.asStateFlow()
}

class JobState