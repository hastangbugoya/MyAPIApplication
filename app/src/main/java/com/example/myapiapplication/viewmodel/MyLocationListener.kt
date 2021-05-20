package com.example.myapiapplication.viewmodel

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class MyLocationListener(private val locationDelegate: LocationDelegate): LocationListener{
    //loose coupling - allows provideLocation to be implemented in MainActivity
    interface LocationDelegate {
        fun provideLocation(location: Location)
    }

    override fun onLocationChanged(location: Location) {
        locationDelegate.provideLocation(location)
    }
    }