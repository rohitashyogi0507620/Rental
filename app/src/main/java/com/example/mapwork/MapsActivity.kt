package com.example.mapwork

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.example.mapwork.adapter.CardStackAdapter
import com.example.mapwork.adapter.Spot
import com.example.mapwork.adapter.SpotDiffCallback
import com.example.mapwork.databinding.ActivityMapsBinding
import com.example.mapwork.ui.activity.HomeActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.yuyakaido.android.cardstackview.*
import `in`.probusinsurance.app.Home.ProductData.BannerAdapter


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener, OnMapsSdkInitializedCallback, CardStackListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)



    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter(createSpots()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_maps)

        setContentView(binding.root)

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val bottomSheet = findViewById<MotionLayout>(R.id.bottom_sheet_behavior_id)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                Log.d("BottomSheet", "State Change ${i.toString()}")

            }

            override fun onSlide(view: View, v: Float) {
                Log.d("BottomSheet", v.toString())
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        //bottomSheetDialog()


       // initialize()
        startActivity(Intent(this,HomeActivity::class.java))


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera

        // Custom Theme
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }

        val sydney = LatLng(26.858631, 75.818909)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
       // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN)

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
        polyline1.tag = "Your Area"
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


    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
        binding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        Log.d("CardStackView", "onCardAppeared: ($position)")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        Log.d("CardStackView", "onCardDisappeared:")
    }


    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): Spot {
        return Spot(
            name = "Yasaka Shrine",
            city = "Kyoto",
            url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
        )
    }

    private fun createSpots(): List<Spot> {
        val spots = ArrayList<Spot>()
        spots.add(Spot(name = "Yasaka Shrine", city = "Kyoto", url = "https://source.unsplash.com/Xq1ntWruZQI/600x800"))
        spots.add(Spot(name = "Fushimi Inari Shrine", city = "Kyoto", url = "https://source.unsplash.com/NYyCqdBOKwc/600x800"))
        spots.add(Spot(name = "Bamboo Forest", city = "Kyoto", url = "https://source.unsplash.com/buF62ewDLcQ/600x800"))
        spots.add(Spot(name = "Brooklyn Bridge", city = "New York", url = "https://source.unsplash.com/THozNzxEP3g/600x800"))
        spots.add(Spot(name = "Empire State Building", city = "New York", url = "https://source.unsplash.com/USrZRcRS2Lw/600x800"))
        spots.add(Spot(name = "The statue of Liberty", city = "New York", url = "https://source.unsplash.com/PeFk7fzxTdk/600x800"))
        spots.add(Spot(name = "Louvre Museum", city = "Paris", url = "https://source.unsplash.com/LrMWHKqilUw/600x800"))
        spots.add(Spot(name = "Eiffel Tower", city = "Paris", url = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"))
        spots.add(Spot(name = "Big Ben", city = "London", url = "https://source.unsplash.com/CdVAUADdqEc/600x800"))
        spots.add(Spot(name = "Great Wall of China", city = "China", url = "https://source.unsplash.com/AWh9C-QjhE4/600x800"))
        return spots
    }
}