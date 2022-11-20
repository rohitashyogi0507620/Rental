package `in`.probusinsurance.app.Home.ProductData

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.mapwork.Banner
import com.example.mapwork.R
import com.example.mapwork.Room
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter


class BannerAdapter(var context: Context, var banneritem: Banner) :
    SliderViewAdapter<BannerAdapter.SliderAdapterVH>() {


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_slider_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val imageUrl: String = banneritem.imageUrl.get(position)

        Glide.with(context).load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewHolder.roomBannerProgress.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewHolder.roomBannerProgress.visibility = View.GONE
                    return false
                }
            })
            .into(viewHolder.roomBanner)


        viewHolder.roomBanner.setOnClickListener {
            Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCount(): Int {
        return banneritem.imageUrl.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {

        var roomBanner: ImageView
        var roomBannerProgress: ProgressBar

        init {
            roomBanner = itemView.findViewById(R.id.room_banner_image)
            roomBannerProgress = itemView.findViewById(R.id.room_banner_progress)
        }
    }

}