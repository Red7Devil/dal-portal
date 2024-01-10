package com.example.dalportal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobListing(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val pay: Int,
    val positions : Int,
    val requirements: String,
    val type:String,
    val tags: List<String>


) : Parcelable
