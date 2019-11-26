package com.example.taxitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled=true

        var stringJsonArray = getAssetJsonData()
        var jsonArray = JSONArray(stringJsonArray)

        LocationService(this)

        for (i in 0 until jsonArray.length()) {
           plotMarkers(jsonArray.optJSONObject(i))
        }

        var obj = jsonArray.get(0) as JSONObject
        obj = obj.optJSONObject("coordinate")

        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(obj.optDouble("latitude"), obj.optDouble("longitude")), 16.0f))

    }

    /**
     * reading json from file
     */
    fun getAssetJsonData(): String? {
        var json: String? = null
        try {
            val istream = assets.open("cabs.json")
            val size = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        Log.e("data", json)
        return json

    }

    fun plotMarkers(jsonObject: JSONObject){

        var latitude = jsonObject.optJSONObject("coordinate").optDouble("latitude")
        var longitude = jsonObject.optJSONObject("coordinate").optDouble("longitude")
        var heading = jsonObject.optString("heading")


        var marker = mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .anchor(0.5f, 0.5f)
                .rotation(heading.toFloat())
                .flat(true)
        )

    }
}
