package com.example.mapwork

import `in`.probusinsurance.app.Home.ProductData.BannerAdapter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class RoomDetailsActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_room_details)

        var banneritem=Banner(listOf(
            "https://media-cdn.tripadvisor.com/media/photo-s/13/d8/ea/1b/a-room-at-the-beach.jpg",
            "https://media-cdn.tripadvisor.com/media/photo-s/15/ca/5c/f5/ruby-room.jpg",
            "https://media-cdn.tripadvisor.com/media/photo-s/0d/e6/a1/55/ruby-room.jpg"))

        var banner=findViewById<SliderView>(R.id.room_banner)
        banner.setSliderAdapter(BannerAdapter(applicationContext,banneritem))
        banner.setIndicatorAnimation(IndicatorAnimationType.WORM);
        banner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        banner.startAutoCycle()
    }
}