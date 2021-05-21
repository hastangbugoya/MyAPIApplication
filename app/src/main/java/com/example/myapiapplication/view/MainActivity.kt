package com.example.myapiapplication.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.myapiapplication.R
import com.example.myapiapplication.model.Result
import com.example.myapiapplication.util.Constants.Companion.LogKitty
import com.example.myapiapplication.util.State
import com.example.myapiapplication.viewmodel.MyLocationListener
import com.example.myapiapplication.viewmodel.PlacesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager //Provides access to the system location services
    private val viewModel: PlacesViewModel by viewModels<PlacesViewModel>()
    lateinit var placesCategorySpinner: Spinner
    val categoryList: List<String> = listOf("restaurant", "school", "church", "library", "atm", "park", "police")
    private var currentCategory: String = categoryList[0]
    private lateinit var currentLocation : Location
    //Recycler
    private lateinit var placesRecycler: RecyclerView
    lateinit var placesRecyclerAdapter: PlacesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Recycler
        placesRecycler = findViewById(R.id.places_recyclerview)
        placesRecyclerAdapter = PlacesRecyclerAdapter()
        placesRecycler.adapter = placesRecyclerAdapter

        //assign view to spinner
        val mySpinner: Spinner = findViewById(R.id.category_spinner)
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
            LogKitty(it.size.toString())
//            for (place in it) {
//                LogKitty(place.name)
//            }
            this.placesRecyclerAdapter.updateList(it.toMutableList())
        })

        viewModel.statusData.observe(this,{
            when(it){
                State.LOADING -> {status_progressbar.visibility = View.VISIBLE}
                State.SUCCESS -> {status_progressbar.visibility = View.GONE}
                State.ERROR -> displayErrorMessage()
                else -> {}
            }
        })

        fun updateList(list: MutableList<Result>)
        {

        }

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
        Geocoder(this, Locale.getDefault()).getFromLocation(location.latitude, location.longitude, 1)
                .also { it[0].getAddressLine(0).let { add ->
                    findViewById<TextView>(R.id.current_address_textview).text = add
                }}
        currentLocation = location
        viewModel.getPlacesNearMe(location,category)
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(myLocationListener)
    }
}