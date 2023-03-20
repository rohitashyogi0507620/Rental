package com.example.mapwork.ui.activity

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mapwork.Banner
import com.example.mapwork.R
import com.example.mapwork.base.BaseActivity
import com.example.mapwork.databinding.ActivityRoomViewBinding
import com.example.mapwork.models.request.PropertyDetailsByIdRequest
import com.example.mapwork.models.response.NetworkResult
import com.example.mapwork.utils.SessionConstants.PROPERTY_ID
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import `in`.probusinsurance.app.Home.ProductData.BannerAdapter
import `in`.probusinsurance.probusdesign.ui.dialog.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class RoomViewActivity : BaseActivity<ActivityRoomViewBinding, RoomViewActivityViewModel>() {

    var propertyId = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_room_view
    }

    override fun buildViewModel(): RoomViewActivityViewModel {
        return ViewModelProvider(this).get(RoomViewActivityViewModel::class.java)
    }

    override fun getBundle() {

        if (intent != null && intent.extras != null) {
            propertyId = intent.extras!!.getString(PROPERTY_ID)!!
            Log.d("PROPERTY_ID", propertyId)
        }

    }

    override fun initViews() {

        if (!propertyId.isNullOrEmpty()) {
            viewModel.getPropertyDetailsById(PropertyDetailsByIdRequest(propertyId))
        }

    }

    override fun initLiveDataObservers() {


        viewModel.propertyDetailsByIdResponse.observe(this) {
            it.getContentIfNotHandled()?.let {
                //binding.progressLayout.setShowProgress(false)

                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data != null) {
                            if (!it.data.images.isNullOrEmpty()) {
                                var banneritem = Banner(it.data.images)
                                binding.roomViewbanner.setSliderAdapter(
                                    BannerAdapter(
                                        applicationContext,
                                        banneritem
                                    )
                                )
                                binding.roomViewbanner.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                binding.roomViewbanner.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                binding.roomViewbanner.startAutoCycle()
                            }
                            binding.roomViewTxttitle.text = it.data.property_type
                            binding.roomViewTxtlocation.text =
                                "${it.data.plot_number} , ${it.data.street} ${it.data.landmark} ,${it.data.city} ,${it.data.state} ,${it.data.pincode}"
                            binding.roomViewRentbtn.text =
                                getString(R.string.rupesssign) + " ${it.data.price}" + getString(R.string.rupesending) + " " + getString(
                                    R.string.month
                                )
                            binding.roomViewTxtdate.text =getString(R.string.addedon)+ getFormatedDate(it.data.date_time)

                        }
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

    private fun getFormatedDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        //val outputFormat = SimpleDateFormat("dd MMM yyyy hh:mm a")
        val outputFormat = SimpleDateFormat("dd MMM yyyy")
        val parsedDate: Date = inputFormat.parse(dateTime)
        val formattedDate: String = outputFormat.format(parsedDate)
        return formattedDate
    }

    override fun onClickListener() {

        binding.roomViewFullscreen.setOnClickListener {
            startActivity(Intent(applicationContext, RoomImagesActivity::class.java))
        }

    }

}