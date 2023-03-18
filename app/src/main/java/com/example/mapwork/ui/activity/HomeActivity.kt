package com.example.mapwork.ui.activity

import RoomShotAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapwork.R
import com.example.mapwork.adapter.InfoWindowAdapter
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityHomeBinding
import com.example.mapwork.models.response.MakerInfoData
import com.example.mapwork.models.response.RoomData
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.IOException
import java.util.*


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), OnMapReadyCallback,
    GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener,
    OnMapsSdkInitializedCallback, GoogleMap.OnInfoWindowClickListener,
    RoomShotAdapter.OnItemClickListener {

    private lateinit var mMap: GoogleMap
    private val PERMISSION_ID = 400

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var adapterShortRoom: RoomShotAdapter
    var list_room = mutableListOf<RoomData>()


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

        binding.contentLayout.homeRoomRecyclearView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterShortRoom = RoomShotAdapter(applicationContext, list_room, this)
        binding.contentLayout.homeRoomRecyclearView.adapter = adapterShortRoom

    }

    override fun initLiveDataObservers() {

        list_room.add(
            RoomData(
                "₹ 5500/-",
                "100",
                "Rajput Room",
                "rajput colony malviya nagar jaipur 302017",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiNbfKmgTK5BhY13X5cy9Yf_E9IP9lqulxlCAdLRxFBGeaUFMtijXgecBh9OXDWrVXVxY&usqp=CAU",
                26.860794, 75.810796
            )
        )
        list_room.add(
            RoomData(
                "₹ 3500/-",
                "100",
                "Vijay Raj Room",
                "C5 malviya nagar Jaipur 302017",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQX-MOoLj1SlW_qBJIxwsZUiHrqXIjbT-uh_A&usqp=CAU",
                26.857949, 75.814630
            )
        )
        list_room.add(
            RoomData(
                "₹ 9500/-",
                "100",
                "Shree Ganesh Room",
                "203 C2 plaza malviya nagar jaipur 302017",
                "https://blog.woodenstreet.com/images/data/image_upload/1652505306living-room-color-combination-idea-banner.jpeg",
                26.858319, 75.813104
            )
        )
        list_room.add(
            RoomData(
                "₹ 9500/-",
                "2116",
                "Mahima Apartment",
                "2nd flor 203no malviya nagar jaipur 302017",
                "https://blog.woodenstreet.com/images/data/image_upload/1652505306living-room-color-combination-idea-banner.jpeg",
                26.8574957, 75.8130546
            )
        )
        adapterShortRoom.notifDataChanged()


        binding.contentLayout.idSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String?): Boolean {

                val location: String = query!!
                var addressList: List<Address>? = null
                if (location != null || location == "") {
                    val geocoder = Geocoder(applicationContext)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


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

            googleMap.setInfoWindowAdapter(InfoWindowAdapter(applicationContext))
            googleMap.setOnInfoWindowClickListener(this)

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }

            mMap.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
                override fun onCameraIdle() {
                    val midLatLng: LatLng = mMap.getCameraPosition().target
                    Log.e("Location", midLatLng.toString())

                }
            })
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

                            if (task != null && task.result != null) {
                                val location: Location = task.result
                                if (location == null) {
                                    requestNewLocationData()
                                } else {


                                    list_room.add(
                                        RoomData(
                                            " ₹ 2900/-",
                                            "100",
                                            "Your Room",
                                            "362 Plaza malviya nagar jaipur 302017",
                                            "https://blog.woodenstreet.com/images/data/image_upload/1652505306living-room-color-combination-idea-banner.jpeg",
                                            task.result.latitude,
                                            task.result.longitude
                                        )
                                    )
                                    adapterShortRoom.notifDataChanged()

                                    list_room.forEach { addMakerToLocation(it) }


                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Location Result not Recived turn on",
                                    Toast.LENGTH_LONG
                                ).show()
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
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
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
        startActivity(Intent(applicationContext, RoomViewActivity::class.java))

    }

    override fun onInfoWindowClick(p0: Marker) {
        Toast.makeText(
            this, "Info window clicked",
            Toast.LENGTH_SHORT
        ).show()
    }


    fun addMakerToLocation(roomData: RoomData) {

        val yourlocation = LatLng(roomData.latitude, roomData.longitude)
        var markerData = MakerInfoData(
            roomData.title,
            roomData.location,
            roomData.price
        )
        val gson = Gson()
        val markerInfoString = gson.toJson(markerData)

        mMap.addMarker(
            MarkerOptions().position(yourlocation).snippet(markerInfoString)
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_singleroom))
        )
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourlocation, 15f))
        getDetailsFromLatLong(roomData.latitude, roomData.longitude)
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getDetailsFromLatLong(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())

        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        val address: String = addresses[0].getAddressLine(0)
        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val zip: String = addresses[0].postalCode
        val featureName: String = addresses[0].featureName
        val country: String = addresses[0].countryName

        Log.d(
            "LocationInfo",
            " latitude ${latitude} longitude ${longitude} address ${address}  :: city ${city} state ${state} zip ${zip} country ${country} featureName ${featureName}"
        )

    }


    fun addCircle(latitude: Double, longitude: Double) {
        val circle = mMap.addCircle(
            CircleOptions()
                .center(LatLng(latitude, longitude))
                .radius(100.0)
                .strokeWidth(5f)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(20, 100, 0, 0))
                .clickable(true)
        )
        mMap.setOnCircleClickListener {
            val strokeColor = it.strokeColor xor 0x00ffffff
            it.strokeColor = strokeColor
        }
    }


}

