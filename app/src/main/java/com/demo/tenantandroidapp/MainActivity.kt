package com.demo.tenantandroidapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    var exit = 0
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Enable location tracking
//        googleMap.isMyLocationEnabled = true
        requestLocationPermission()
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        // Add custom bike locations
        val bikeLocations = listOf(
            BikeLocation(LatLng(22.3010356,73.2304143), "Bike Location 1"),
            BikeLocation(LatLng(22.3010989,73.2304546), "Bike Location 2"),
            BikeLocation(LatLng(22.3012289, 73.2299699), "Bike Location 3"),
            BikeLocation(LatLng(22.301835,73.2301593), "Bike Location 4"),
            // Add more bike locations as needed
        )

        // Add markers for each bike location
        for (location in bikeLocations) {
            val markerOptions = MarkerOptions()
                .position(location.latLng)
                .title(location.title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_water_dispenser))
            googleMap.addMarker(markerOptions)
        }

        // Move camera to a default location
        val defaultLocation = LatLng(22.3012289, 73.2299699)
        val initialCameraPosition = CameraPosition.Builder()
            .target(defaultLocation)
            .zoom(16f)
            .build()
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(initialCameraPosition))
        // Set up touch listeners for map interactions
        googleMap.setOnMapClickListener { latLng ->
            // Handle map click events here
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.cameraPosition.zoom + 1))

        }

        googleMap.setOnMarkerClickListener { marker ->
            // Handle marker click events here
            false // Return false to allow default behavior (e.g., open InfoWindow)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    // Request location permission
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted
            enableMyLocation()
        }
    }

//    // Handle permission result
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Location permission granted
//                enableMyLocation()
//            } else {
//                // Location permission denied
//                // Handle accordingly (e.g., show a message, disable location-related functionality)
//            }
//        }
//    }

    // Enable my location on Google Maps
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
    }
}