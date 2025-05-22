package com.example.myapplication.ui

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map_LBL_title: MaterialTextView
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        map_LBL_title = view.findViewById(R.id.map_LBL_title)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        val defaultLatLng = LatLng(32.0853, 34.7818)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12f))
    }

        fun moveToHighScoreLocation(lat: Double, lon: Double) {
            val latLng = LatLng(lat, lon)
            googleMap?.addMarker(MarkerOptions().position(latLng).title("🎯 High Score Location"))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            map_LBL_title.text = buildString {
                append("🎯\n")
                append(lat)
                append(",\n")
                append(lon)
            }
        }

    }





