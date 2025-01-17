package com.example.doglocation

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.location.Geocoder
import android.location.Location
import java.util.Locale

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager : LocationManager
    private lateinit var tyOutput : TextView
    private lateinit var locationTextView : TextView
    private  var locationPermissionCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button : Button = findViewById(R.id.btnLocation)

        button.setOnClickListener {
            getLocation()
        }
    }

    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if((ContextCompat.checkSelfPermission(
            this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode
            )
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,5f,this)

        }
    }

    override fun onLocationChanged(location: Location){
        tyOutput = findViewById(R.id.lbOutput)
        tyOutput.text="Latitude: " + location.latitude + ", \nLongitude: " + location.longitude
        getAddressFromLocation(location)
    }

    private fun getAddressFromLocation(location: Location){
        val geocoder = Geocoder(this, Locale.getDefault())
        locationTextView = findViewById(R.id.textView)
        try{
            val addresses = geocoder.getFromLocation(location.latitude , location.longitude,1)
            if (addresses != null && addresses.isNotEmpty()){
                val address = addresses[0]
                val addressLine = address.getAddressLine(0)
                locationTextView.text = "Address: $addressLine"
            }else{
                locationTextView.text = "Unable to get address"
            }
        }
        catch(e:Exception){
            e.printStackTrace()
            locationTextView.text="Error getting address"
        }
    }
}