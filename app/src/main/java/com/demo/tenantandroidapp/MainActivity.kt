package com.demo.tenantandroidapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val locationPermissionCode = 123
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocationTextView: EditText
    private lateinit var dropLocationTextView: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationGlobal: Location

    private lateinit var polyline: Polyline
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById<MapView>(R.id.mapView)
        currentLocationTextView = findViewById(R.id.currentLocationTextView)
        dropLocationTextView = findViewById(R.id.dropLocationTextView)

//        if (isGoogleMapsInstalled()) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
//        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(map: GoogleMap) {
        map.run {
            googleMap = map
            requestLocationPermission()
            //            googleMap.isMyLocationEnabled = true
            val databaseReference = FirebaseDatabase.getInstance().getReference("riders")
            Log.d("TAG", "databaseReference::::$databaseReference")
            databaseReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    val riderId = dataSnapshot.key // Get the auto-generated key
                    // Retrieve the "lat" and "long" values from the "location" key
                    val locationReference = riderId?.let { databaseReference.child(it).child("location") }
                    locationReference?.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val lat = dataSnapshot.child("lat").value as Double
                            val lng = dataSnapshot.child("long").value as Double
                            Log.d("TAG", "checkLat::::$lat::::checkLong:::::::$lng")
                            val bikeLocation = LatLng(lat, lng)
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(bikeLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_water_dispenser))
                            )
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d("TAG", "Failed to read value: ${databaseError.toException()}")
                        }
                    })
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    //                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    //                    TODO("Not yet implemented")
                }

            })

            val ridersRef = FirebaseDatabase.getInstance().getReference("riders")
            ridersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Iterate through the riders and calculate distance from the user's location
//                    Log.d("TAG", "checksnapshot::::::::$snapshot")
//                    val rider1 = snapshot.getValue(Rider::class.java)
//                    Log.d("TAG", "rider1::::::::$rider1")

                    for (riderSnapshot in snapshot.children) {
//                        val rider = riderSnapshot.getValue(Rider::class.java)
                        // Calculate distance between userLocation and rider's location
//                        val distance = calculateDistance(locationGlobal, rider!!.location)
                        Log.d("TAG", "checkDistance:::::${riderSnapshot.getValue(com.demo.tenantandroidapp.Location::class.java)}")
val locationlatlong = riderSnapshot.child("location")
              val getlatlong =          locationlatlong.value
                        Log.d("TAG", "getlatlog::::::::${getlatlong}")

                        // Store the distance and rider information for further processing
                        // ...
                    }
                    // Allocate riders based on distance or other criteria
                    // ...
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any database errors
                    // ...
                }
            })
            // Add markers for allocated riders on the map
//            for (rider in allocatedRiders) {
//                val markerOptions = MarkerOptions()
//                    .position(LatLng(rider.location.latitude, rider.location.longitude))
//                    .title(rider.name)
//                googleMap.addMarker(markerOptions)
//            }

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

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                enableMyLocation()
            } else {
                // Location permission denied
                // Handle accordingly (e.g., show a message, disable location-related functionality)
            }
        }
    }

    // Enable my location on Google Maps
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            // Get the user's last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLocation = LatLng(it.latitude, it.longitude)
                    locationGlobal = it
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(currentLocation)
                            .title("Current Location")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }
            }
        }
    }

    fun calculateDistance(
        location1: Location,
        location2: com.demo.tenantandroidapp.Location
    ): Float {
        val result = FloatArray(1)
        Location.distanceBetween(
            location1.latitude,
            location1.longitude,
            location2.lat,
            location2.long,
            result
        )
        return result[0]
    }


}