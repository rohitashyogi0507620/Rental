package com.example.mapwork.ui.activity

import RecentSearchAdapter
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
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapwork.R
import com.example.mapwork.SearchActivity
import com.example.mapwork.adapter.InfoWindowAdapter
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityHomeBinding
import com.example.mapwork.models.request.PropertyByPincodeRequest
import com.example.mapwork.models.response.MakerInfoData
import com.example.mapwork.models.response.NetworkResult
import com.example.mapwork.models.response.PropertyDataResponse
import com.example.mapwork.models.response.RecentSearch
import com.example.mapwork.utils.SessionConstants
import com.example.mapwork.utils.SessionConstants.PROPERTY_ID
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import `in`.probusinsurance.probusdesign.ui.dialog.AlertDialog
import java.util.*


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), OnMapReadyCallback,
    GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener,
    OnMapsSdkInitializedCallback, GoogleMap.OnInfoWindowClickListener,
    RoomShotAdapter.OnItemClickListener, RecentSearchAdapter.OnItemClickListener {

    private lateinit var mMap: GoogleMap
    private val PERMISSION_ID = 400

    private val PATTERN_GAP_LENGTH_PX = 20
    private val ZOOM_LEVEL = 15f
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var adapterShortRoom: RoomShotAdapter
    lateinit var adapterRecentSearch: RecentSearchAdapter
    var list_room = mutableListOf<PropertyDataResponse>()
    var recentsearch_list = mutableListOf<RecentSearch>()


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

        binding.contentLayout.homeRecentRecyclearView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterRecentSearch = RecentSearchAdapter(applicationContext, recentsearch_list, this)
        binding.contentLayout.homeRecentRecyclearView.adapter = adapterRecentSearch

    }

    override fun initLiveDataObservers() {


        viewModel.propertyDataResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                //binding.progressLayout.setShowProgress(false)

                when (it) {
                    is NetworkResult.Success -> {
                        list_room.removeAll(list_room)
                        list_room.addAll(it.data!!)
                        list_room.forEach {
                            addPropertyMarkerToMap(it)
                        }
                        adapterShortRoom.notifDataChanged()

                    }
                    is NetworkResult.Error -> {
                        AlertDialog.ErrorDialog(this, it.errrormessage.toString())
                    }
                    is NetworkResult.Loading -> {
                        // binding.progressLayout.setShowProgress(true)

                    }
                    else -> {

                    }
                }
            }
        }

        viewModel.recentSearchResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                //binding.progressLayout.setShowProgress(false)

                when (it) {
                    is NetworkResult.Success -> {
                        recentsearch_list.addAll(it.data!!)
                        adapterRecentSearch.notifDataChanged()

                    }
                    is NetworkResult.Error -> {
                        AlertDialog.ErrorDialog(this, it.errrormessage.toString())
                    }
                    is NetworkResult.Loading -> {
                        // binding.progressLayout.setShowProgress(true)

                    }
                    else -> {

                    }
                }
            }
        }


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

        binding.contentLayout.homeChipgroupCommon.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.equals(R.id.home_chip_singleRoom)) {

            } else if (checkedIds.equals(R.id.home_chip_Roomkitchen)) {

            } else if (checkedIds.equals(R.id.home_chip_Bachelors)) {

            }
        }

        binding.contentLayout.homeSearchLayout.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }


    private fun showBottomSheetModify() {
        Snackbar.make(binding.root, "Modify", Snackbar.LENGTH_LONG).show()
    }

    private fun showBottomSheetFilter() {
        var bottomSheetDialog: BottomSheetDialog? =
            BottomSheetDialog(this, R.style.BottomSheetDialogtheme)
        val view: View = LayoutInflater.from(this).inflate(R.layout.layout_bottomsheet_filter, null)
        bottomSheetDialog!!.setContentView(view)

        bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val window: Window = bottomSheetDialog!!.window!!
        val decorView: View = window.getDecorView()
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        bottomSheetDialog!!.show()


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


            mMap.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
                override fun onCameraIdle() {

//                    val midLatLng: LatLng = mMap.getCameraPosition().target
//                    if (midLatLng != null && !midLatLng.latitude.equals(0.0) && !midLatLng.longitude.equals(0.0)) {
//                        var address = getAddressFromLatLong(midLatLng.latitude, midLatLng.longitude)
//                        if (!address.isNullOrEmpty()) {
//                            Log.e("PointerAddress", "Your Pointer ${address}")
//                            binding.contentLayout.homeTxtsearchview.setText(address)
//                        }
//                    }

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
                                    addMakerToOwnLocation(
                                        task.result.latitude,
                                        task.result.longitude
                                    )
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
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.getLastLocation()!!
            Log.d(
                "Location",
                "On Location Change Latitude ${mLastLocation.getLatitude()} and Longitude ${mLastLocation.getLongitude()}"
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

    override fun onItemClick(item: PropertyDataResponse, position: Int, type: Int) {
        startActivity(
            Intent(applicationContext, RoomViewActivity::class.java).putExtra(
                PROPERTY_ID,
                item.id.toString()
            )
        )
    }

    override fun onInfoWindowClick(p0: Marker) {
        Toast.makeText(
            this, "Info window clicked", Toast.LENGTH_SHORT
        ).show()
    }



    fun getAddressFromLatLong(latitude: Double, longitude: Double): String? {
        try {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty() && addresses.size > 0) {
                val address: String = addresses[0].getAddressLine(0)
                return address
            } else {
                return null
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun addCircle(latitude: Double, longitude: Double) {
        val circle = mMap.addCircle(
            CircleOptions().center(LatLng(latitude, longitude)).radius(100.0).strokeWidth(5f)
                .strokeColor(Color.GREEN).fillColor(Color.argb(20, 100, 0, 0)).clickable(true)
        )
        mMap.setOnCircleClickListener {
            val strokeColor = it.strokeColor xor 0x00ffffff
            it.strokeColor = strokeColor
        }
    }


    fun addMakerToOwnLocation(latitude: Double, longitude: Double) {

        try {

            val yourlocation = LatLng(latitude, longitude)
            mMap.addMarker(
                MarkerOptions().position(yourlocation)
                    .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_userlocation))
            )
            moveCameraToLocation(latitude, longitude)

            var zipCode = getPincodeLatLong(latitude, longitude)!!
            if (!zipCode.isNullOrEmpty()) {
                viewModel.getPropertyByPincode(PropertyByPincodeRequest(zipCode))
            }
            var address = getAddressFromLatLong(latitude, longitude)
            if (!address.isNullOrEmpty()) {
                Log.e("PointerAddress", "Your Pincode ${zipCode} Your Pointer ${address}")
                binding.contentLayout.homeTxtsearchview.setText(address)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun addPropertyMarkerToMap(propertyData: PropertyDataResponse) {

        val yourlocation = LatLng(propertyData.latitude, propertyData.longtitude)
        var markerData = MakerInfoData(
            propertyData.property_type,
            "${propertyData.plot_number} ${propertyData.street}",
            getString(R.string.rupesssign) + " ${propertyData.price}" + getString(R.string.rupesending)

        )
        val gson = Gson()
        val markerInfoString = gson.toJson(markerData)

        mMap.addMarker(
            MarkerOptions().position(yourlocation).snippet(markerInfoString)
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_singleroom))
        )

    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
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

    fun getPincodeLatLong(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty() && addresses.size > 0) {
            val zip: String = addresses[0].postalCode
            return zip
        } else {
            return null
        }
    }

    fun moveCameraToLocation(latitude: Double, longitude: Double) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), ZOOM_LEVEL))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
    }

    override fun onItemClick(item: RecentSearch, position: Int, type: Int) {
        Toast.makeText(applicationContext, item.toString(), Toast.LENGTH_SHORT)
    }


}

