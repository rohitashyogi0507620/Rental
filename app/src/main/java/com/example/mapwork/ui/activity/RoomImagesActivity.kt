package com.example.mapwork.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mapwork.Banner
import com.example.mapwork.R
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityRoomViewBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import `in`.probusinsurance.app.Home.ProductData.BannerAdapter

class RoomImagesActivity: BaseActivity<ActivityRoomViewBinding, RoomImagesActivityViewModel>() {
    override fun getLayoutId(): Int {
        return  R.layout.activity_room_images
    }

    override fun buildViewModel(): RoomImagesActivityViewModel {
        return ViewModelProvider(this).get(RoomImagesActivityViewModel::class.java)
    }

    override fun getBundle() {
    }

    override fun initViews() {

    }

    override fun initLiveDataObservers() {

    }

    override fun onClickListener() {


    }
}