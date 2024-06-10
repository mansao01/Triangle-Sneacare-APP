package com.mansao.trianglesneacare.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object MapsNavigateUtil{

    fun pinLocationMap(latitude: String, longitude: String, context: Context) {
        // Create a Uri from an intent string. Open map using intent to pin a specific location (latitude, longitude)
        val mapUri = Uri.parse("https://maps.google.com/maps/search/$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        context.startActivity(intent)
    }
}