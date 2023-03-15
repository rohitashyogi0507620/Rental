package com.example.mapwork.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mapwork.Banner
import com.example.mapwork.R
import com.example.mapwork.Room
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityHomeBinding
import com.example.mapwork.databinding.ActivityRoomViewBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import `in`.probusinsurance.app.Home.ProductData.BannerAdapter

class RoomViewActivity : BaseActivity<ActivityRoomViewBinding, RoomViewActivityViewModel>() {
    override fun getLayoutId(): Int {
        return  R.layout.activity_room_view
    }

    override fun buildViewModel(): RoomViewActivityViewModel {
        return ViewModelProvider(this).get(RoomViewActivityViewModel::class.java)
    }

    override fun getBundle() {
    }

    override fun initViews() {
        var banneritem= Banner(listOf(
            "https://media-cdn.tripadvisor.com/media/photo-s/13/d8/ea/1b/a-room-at-the-beach.jpg",
            "https://media-cdn.tripadvisor.com/media/photo-s/15/ca/5c/f5/ruby-room.jpg",
            "https://media-cdn.tripadvisor.com/media/photo-s/0d/e6/a1/55/ruby-room.jpg"))

        binding.roomViewbanner.setSliderAdapter(BannerAdapter(applicationContext,banneritem))
        binding.roomViewbanner.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.roomViewbanner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.roomViewbanner.startAutoCycle()

    }

    override fun initLiveDataObservers() {

    }

    override fun onClickListener() {

        binding.roomViewFullscreen.setOnClickListener {
           startActivity(Intent(applicationContext,RoomImagesActivity::class.java))
        }

    }
}