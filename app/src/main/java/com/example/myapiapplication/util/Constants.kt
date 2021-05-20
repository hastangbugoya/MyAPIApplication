package com.example.myapiapplication.util

import android.util.Log

class Constants {
//    companion objects -> static in java
    companion object{
//        Created in Google Maps
        //const val API_KEY = "AIzaSyDv0ls1Rjx1da8pMUxFNsCWSKGfJNnm6dk" //mine
        const val API_KEY = "AIzaSyA5rJh9FeteViW7n9M3iORS7d8L7W75wVI" //Dalo

        fun LogKitty(message : String )  {
            Log.d("Meow", message)
        }
    }
}