package com.mansao.trianglesneacare.location

import android.location.Location

interface LocationCallback {
    fun onLocationResult(location: Location)
}