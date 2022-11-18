package com.example.mapwork

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.mapwork.databinding.ActivityMapsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener ,OnMapsSdkInitializedCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    // Create a stroke pattern of a gap followed by a dot.
    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MapsInitializer.initialize(applicationContext,MapsInitializer.Renderer.LATEST,this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val bottomSheet = findViewById<MotionLayout>(R.id.bottom_sheet_behavior_id)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                Log.d("BottomSheet","State Change ${i.toString()}")

            }

            override fun onSlide(view: View, v: Float) {
                Log.d("BottomSheet",v.toString())
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

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
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(26.858631, 75.818909)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        val polyline1 = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(26.860956, 75.806386),
                    LatLng(26.867655, 75.808145),
                    LatLng(26.866086, 75.821320),
                    LatLng(26.855596, 75.821664),
                    LatLng(26.860458, 75.806558)
                )
        )
        polyline1.tag="Your Area"
        stylePolyline(polyline1)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(26.860956, 75.806386), 13f))

//        val polygon = googleMap.addPolygon(
//            PolygonOptions()
//                .clickable(true)
//                .add(
//                    LatLng(26.860920, 75.8063696),
//                    LatLng(26.867610, 75.808165),
//                    LatLng(26.866095, 75.821325),
//                    LatLng(26.855593, 75.821674),
//                    LatLng(26.860448, 75.806568)
//                )
//        )
//        polygon.tag="Your Area"
       // stylePolygon(polygon)
        // Set listeners for click events.

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)


    }





    // Poly Line
    private val COLOR_BLACK_ARGB = -0x19350000
    private val POLYLINE_STROKE_WIDTH_PX = 5
    private fun stylePolyline(polyline: Polyline) {
        val type = polyline.tag?.toString() ?: ""
        when (type) {
            "A" -> {
                // Use a custom bitmap as the cap at the start of the line.
                polyline.startCap = CustomCap(
                    BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_camera), 10f)
            }
            "B" -> {
                // Use a round cap at the start of the line.
                polyline.startCap = RoundCap()
            }
        }
        polyline.endCap = RoundCap()
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = COLOR_BLACK_ARGB
        polyline.jointType = JointType.ROUND
    }

    //Add PolyGon
    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_DARK_GREEN_ARGB = -0xc771c4
    private val COLOR_LIGHT_GREEN_ARGB = -0x7e387c
    private val COLOR_DARK_ORANGE_ARGB = -0xa80e9
    private val COLOR_LIGHT_ORANGE_ARGB = -0x657db
    private val POLYGON_STROKE_WIDTH_PX = 8
    private val PATTERN_DASH_LENGTH_PX = 20

    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val PATTERN_POLYGON_ALPHA = listOf(GAP, DASH)
    private val PATTERN_POLYGON_BETA = listOf(DOT, GAP, DASH, GAP)

    private fun stylePolygon(polygon: Polygon) {
        // Get the data object stored with the polygon.
        val type = polygon.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB
        when (type) {
            "alpha" -> {
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA
                strokeColor = COLOR_DARK_GREEN_ARGB
                fillColor = COLOR_LIGHT_GREEN_ARGB
            }
            "beta" -> {
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA
                strokeColor = COLOR_DARK_ORANGE_ARGB
                fillColor = COLOR_LIGHT_ORANGE_ARGB
            }
        }
        polygon.strokePattern = pattern
        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }




    override fun onPolylineClick(polyline: Polyline) {
        if (polyline.pattern == null || !polyline.pattern!!.contains(DOT)) {
            polyline.pattern = PATTERN_POLYLINE_DOTTED
        } else {
            // The default pattern is a solid stroke.
            polyline.pattern = null
        }
        Toast.makeText(this, polyline.tag.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onPolygonClick(polygon: Polygon) {

        Toast.makeText(this, polygon.tag.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {

    }
}