package com.example.mapsandlocationsdemo

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

const val ZOOM_INCREMENT = .5f

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
                moveFocusToLatLng(latlng, "Manually Entered")
            }
            R.id.zoomIn -> {
                val currentZoom = mMap.cameraPosition.zoom
                mMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom + ZOOM_INCREMENT))

            }
            R.id.zoomOut -> {
                val currentZoom = mMap.cameraPosition.zoom
                mMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom - ZOOM_INCREMENT))
            }
            R.id.getGeoCodingBtn -> {locateByGeoCoding(etSearchByLocation.text.toString())}
        }

    }

    private fun moveFocusToLatLng(latLng: LatLng, title : String) {
        mMap.addMarker(MarkerOptions().position(latLng).title("Moved by User!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    fun locateByGeoCoding(locationToSearch : String){
        val geocoder = Geocoder(this)
        val locationResult = geocoder.getFromLocationName(locationToSearch, 1)
        val lat = locationResult[0].latitude
        val lng = locationResult[0].longitude
        val title = locationResult[0].countryName

        moveFocusToLatLng(LatLng(lat,lng),title)

    }
}
