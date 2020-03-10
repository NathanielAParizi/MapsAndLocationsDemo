package com.example.mapsandlocationsdemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

const val ZOOM_INCREMENT = .5f
vonst val MY_PERMISSIONS_REUEST_FINE_LOCALE = 604

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private var mapFlag =0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestsPermissions()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Krikey Mate!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnGotoLatLng -> {
                val lat = etLat.text.toString().toDouble()
                val lng = etLng.text.toString().toDouble()
                val latlng = LatLng(lat, lng)
                moveFocusToLatLng(latlng, locateByReverseGeocoding(latlng))
            }
            R.id.zoomIn -> {
                val currentZoom = mMap.cameraPosition.zoom
                mMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom + ZOOM_INCREMENT))

            }
            R.id.zoomOut -> {
                val currentZoom = mMap.cameraPosition.zoom
                mMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom - ZOOM_INCREMENT))
            }
            R.id.getGeoCodingBtn -> {
                locateByGeoCoding(etSearchByLocation.text.toString())
            }
            R.id.changeMapTypes -> {

                when(mapFlag){
                    0 -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    1 -> mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                    2 -> mMap.mapType = GoogleMap.MAP_TYPE_NONE
                    3 -> mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    4 -> mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
                if(mapFlag == 4){
                    mapFlag =0
                }else{
                    mapFlag++
                }

            }
        }

    }

    private fun moveFocusToLatLng(latLng: LatLng, title: String) {
        mMap.addMarker(MarkerOptions().position(latLng).title(etSearchByLocation.text.toString()))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    fun locateByGeoCoding(locationToSearch: String) {
        val geocoder = Geocoder(this)
        val locationResult = geocoder.getFromLocationName(locationToSearch, 1)
        val lat = locationResult[0].latitude
        val lng = locationResult[0].longitude
        val title = locationResult[0].countryName

        moveFocusToLatLng(LatLng(lat, lng), title)

    }

    private fun locateByReverseGeocoding(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return results[0].getAddressLine(0).toString()
    }

  fun requestsPermissions(){
      // Here, thisActivity is the current activity
      if (ContextCompat.checkSelfPermission(this,
                      Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {

          // Permission is not granted
          // Should we show an explanation?
          if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                          Manifest.permission.ACCESS_FINE_LOCATION)) {
              // Show an explanation to the user *asynchronously* -- don't block
              // this thread waiting for the user's response! After the user
              // sees the explanation, try again to request the permission.
          } else {
              // No explanation needed, we can request the permission.
              ActivityCompat.requestPermissions(this,
                      arrayOf(Manifest.permission.READ_CONTACTS),
                      MY_PERMISSIONS_REUEST_FINE_LOCALE)

              // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
              // app-defined int constant. The callback method gets the
              // result of the request.
          }
      } else {
          // Permission has already been granted
          Toast.makeText(this, "Permission has already been granted", LENGTH_LONG).show()


      }
  }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REUEST_FINE_LOCALE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Toast.makeText(this, "Permission has beeen granted", LENGTH_LONG).show()

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission was rejected", LENGTH_LONG).show()

                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

}
