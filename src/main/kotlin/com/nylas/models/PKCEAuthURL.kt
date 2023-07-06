package com.nylas.models

data class PKCEAuthURL(
  val url: String,
  val secret: String,
  val secretHash: String,
)
