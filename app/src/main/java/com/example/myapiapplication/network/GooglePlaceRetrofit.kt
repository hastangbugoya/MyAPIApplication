package com.example.myapiapplication.network

import com.example.myapiapplication.model.PlacesResponse
import com.example.myapiapplication.util.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Sample places api call
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.194000,-85.396348&radius=1500&key=AIzaSyA5rJh9FeteViW7n9M3iORS7d8L7W75wVI

class GooglePlaceRetrofit {
    //i
    private val placeEndPoint = createRetrofit().create(PlaceEndPoint::class.java) //iv

    //ii
     private fun createRetrofit(): Retrofit = Retrofit.Builder()
         .baseUrl("https://maps.googleapis.com/")
         .addConverterFactory(GsonConverterFactory.create()) //v
         .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    //iii & vii
//    suspend fun makeApiCallAsync(type: String, keyword: String, location: String, radius: Int) : Deferred<PlacesResponse>{
//        return placeEndPoint.getPlaces(type, Constants.API_KEY, keyword, location, radius)
//    }
    suspend fun makeApiCallAsync(type: String, location: String, radius: Int) : Deferred<PlacesResponse>{
        return placeEndPoint.getPlaces(type, Constants.API_KEY, location, radius)
    }
    //vi
    interface PlaceEndPoint {
        @GET("/maps/api/place/nearbysearch/json")
        fun getPlaces(
            @Query("type") placeType: String,
            @Query("key") apiKey: String,
           // @Query("keyword") keyword: String,
            @Query("location") location: String,
            @Query("radius") radius: Int
        ): Deferred<PlacesResponse> //vii
    }
}