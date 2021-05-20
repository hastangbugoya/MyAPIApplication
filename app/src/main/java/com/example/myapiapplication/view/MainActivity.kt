package com.example.myapiapplication.view

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import com.example.myapiapplication.R
import com.example.myapiapplication.util.Constants.Companion.LogKitty
import com.example.myapiapplication.util.State
import com.example.myapiapplication.viewmodel.MyLocationListener
import com.example.myapiapplication.viewmodel.PlacesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager //Provides access to the system location services
    private val viewModel: PlacesViewModel by viewModels<PlacesViewModel>()
    lateinit var placesCategorySpinner: Spinner
    val categoryList: List<String> = listOf("restaurant", "school", "church")
    private var currentCategory: String = categoryList[0]
    private lateinit var currentLocation : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mySpinner: Spinner = findViewById(R.id.category_spinner)
        LogKitty("Hello")
        //assign view to spinner
        placesCategorySpinner = findViewById(category_spinner.id)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, categoryList
        )

        //setup adapter (fragment??? {requireContext() or requireActivity() instead of this or activity)
        val categorySpinnerAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, categoryList)
        placesCategorySpinner.adapter = categorySpinnerAdapter

        //observe and whenever liveData changes do these
        viewModel.liveData.observe(this, {
//            <Type>! : They're called platform types and they mean that Kotlin doesn't know whether that value can or cannot be null and it's up to you to decide if it's nullable or not
            LogKitty("observe")
            LogKitty(it.size.toString())
            for (place in it) {
                LogKitty(place.name)
            }

        })

        viewModel.statusData.observe(this,{
            when(it){
                State.LOADING -> {}
                State.SUCCESS -> {}
                State.ERROR -> displayErrorMessage()
                else -> {}
            }
            status_progressbar.visibility = View.GONE
        })

        placesCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentCategory = categoryList[position]
                Toast.makeText(parent?.context, currentCategory,Toast.LENGTH_SHORT).show()
                makeApiCall(currentLocation,currentCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun displayErrorMessage() {
        LogKitty("Error in API call...")
        status_progressbar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            5F,
            myLocationListener)
    }

    private val myLocationListener = MyLocationListener(
        object: MyLocationListener.LocationDelegate {
            override fun provideLocation(location: Location) {
                makeApiCall(location, currentCategory)
            }
        }
    )

    private fun makeApiCall(location: Location, category: String) {
        currentLocation = location
        viewModel.getPlacesNearMe(location,category)
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(myLocationListener)
    }
}