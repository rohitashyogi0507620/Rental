package com.example.mapwork.ui.activity

import RoomShotAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapwork.R
import com.example.mapwork.adapter.RoomInfoWindowAdapter
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityHomeBinding
import com.example.mapwork.models.response.RoomData
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), OnMapReadyCallback,
    GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener,
    OnMapsSdkInitializedCallback, RoomShotAdapter.OnItemClickListener {

    private lateinit var mMap: GoogleMap
    private val PERMISSION_ID = 400

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var adapterShortRoom: RoomShotAdapter
     var list_room= mutableListOf<RoomData>()

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun buildViewModel(): HomeActivityViewModel {
        return ViewModelProvider(this).get(HomeActivityViewModel::class.java)
    }

    override fun getBundle() {
    }

    override fun initViews() {

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.contentLayout.homeRoomRecyclearView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        adapterShortRoom = RoomShotAdapter(applicationContext, list_room, this)
        binding.contentLayout.homeRoomRecyclearView.adapter = adapterShortRoom

    }

    override fun initLiveDataObservers() {

        list_room.add(RoomData("5500/- Month","100","Rajput Room","rajput colony malviya nagar jaipur 302017","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiNbfKmgTK5BhY13X5cy9Yf_E9IP9lqulxlCAdLRxFBGeaUFMtijXgecBh9OXDWrVXVxY&usqp=CAU"))
        list_room.add(RoomData("3500/- Month","100","Vijay Raj Room","C5 malviya nagar Jaipur 302017","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQX-MOoLj1SlW_qBJIxwsZUiHrqXIjbT-uh_A&usqp=CAU"))
        list_room.add(RoomData("9500/- month","100","Shree Ganesh Room","203 C2 plaza malviya nagar jaipur 302017","https://blog.woodenstreet.com/images/data/image_upload/1652505306living-room-color-combination-idea-banner.jpeg"))
        binding.contentLayout.homeRoomRecyclearView.adapter?.notifyDataSetChanged()
    }

    override fun onClickListener() {

        binding.contentLayout.homeLocationIcon.setOnClickListener {
            getLastLocation()
        }

        binding.contentLayout.homeFilterIcon.setOnClickListener {
            showBottomSheetFilter()
        }

        binding.contentLayout.homeModifyIcon.setOnClickListener {
            showBottomSheetModify()
        }

    }

    private fun showBottomSheetModify() {
        Snackbar.make(binding.root, "Modify", Snackbar.LENGTH_LONG).show()
    }

    private fun showBottomSheetFilter() {
        Snackbar.make(binding.root, "Filter", Snackbar.LENGTH_LONG).show()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera

        // Custom Theme
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
            googleMap.setInfoWindowAdapter(RoomInfoWindowAdapter(this))

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }

//        val sydney = LatLng(26.858631, 75.818909)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN)

//        val polyline1 = googleMap.addPolyline(
//            PolylineOptions()
//                .clickable(true)
//                .add(
//                    LatLng(26.860956, 75.806386),
//                    LatLng(26.867655, 75.808145),
//                    LatLng(26.866086, 75.821320),
//                    LatLng(26.855596, 75.821664),
//                    LatLng(26.860458, 75.806558)
//                )
//        )
//        polyline1.tag = "Your Area"
//        stylePolyline(polyline1)
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(26.860956, 75.806386), 13f))

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
                    BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_camera), 10f
                )
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


    override fun onPolylineClick(p0: Polyline) {
    }

    override fun onPolygonClick(p0: Polygon) {
    }

    override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given

        try {

            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled()) {


                    mFusedLocationClient.getLastLocation()
                        .addOnCompleteListener(OnCompleteListener { task ->

                            if (task != null) {
                                val location: Location = task.result
                                if (location == null) {
                                    requestNewLocationData()
                                } else {
                                    val yourlocation =
                                        LatLng(location.getLatitude(), location.getLongitude())
                                    mMap.addMarker(
                                        MarkerOptions().position(yourlocation)
                                            .title("Your Location")
                                    )
                                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(yourlocation))
                                    mMap.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(yourlocation, 15f)
                                    )

                                    Log.d(
                                        "Location",
                                        "Latitude ${location.getLatitude()} and Longitude ${location.getLongitude()}"
                                    )
                                }
                            }
                        })
                } else {
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.getLastLocation()!!

            Log.d(
                "Location",
                "Latitude ${mLastLocation.getLatitude()} and Longitude ${mLastLocation.getLongitude()}"
            )

        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }

    override fun onItemClick(item: RoomData) {
        Toast.makeText(applicationContext,item.toString(),Toast.LENGTH_LONG).show()
        startActivity(Intent(applicationContext,RoomViewActivity::class.java))
    }

}

