package com.leoarmelin.connecta.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Word(
    val value: String,
    val category: String
)
