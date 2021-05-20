package com.example.myapiapplication.viewmodel

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapiapplication.model.Result
import com.example.myapiapplication.network.GooglePlaceRetrofit
import com.example.myapiapplication.util.Constants.Companion.LogKitty
import com.example.myapiapplication.util.FunctionUtility.Companion.toFormatedString
import com.example.myapiapplication.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class PlacesViewModel: ViewModel() {
    //8:viii
    private var netJob: Job? = null

    private val retrofit = GooglePlaceRetrofit() // GooglePlaceRetrofit from the network package

    val liveData: MutableLiveData<List<Result>> = MutableLiveData<List<Result>>() //liveData can hold more than one Result: multiple API calls

    val statusData = MutableLiveData<State>()

    fun getPlacesNearMe(location: Location, category: String) {
        statusData.value = State.LOADING
        //viewModelScope - Gradle requires: implementation "androidx.activity:activity-ktx:1.2.3"
        try {
            netJob =  this.viewModelScope.launch(Dispatchers.IO) {
                val result = retrofit.makeApiCallAsync(
                        category,
                        //"chicken",
                        location.toFormatedString(),
                        10000
                ).await()
                //wait for the result of the query
                liveData.postValue(result.results) //postValue - value is set from a background thread vs setValue -> main thread
                statusData.postValue(State.SUCCESS)
            }
        } catch (e: Exception) {
            LogKitty(e.toString())
            statusData.postValue(State.ERROR)
        }
    }

    override fun onCleared() {
        //cancel job: if active 8:viii
        netJob?.cancel()
        super.onCleared()
    }

    fun onProviderEnabled(provider: String) {}

    fun onProviderDisabled(provider: String) {}

    fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}