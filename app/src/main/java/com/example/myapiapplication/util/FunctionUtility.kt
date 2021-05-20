package com.example.myapiapplication.util

import android.location.Location

class FunctionUtility {
    companion object {
//        concatenates longitude and latitude into comma separated string
//        Kotlin Extension Function - add function to existing class without inheriting
//        Location is an imported class to which function toFormatString is added
        fun Location.toFormatedString(): String = "${this.latitude},${this.longitude}"

    }
}