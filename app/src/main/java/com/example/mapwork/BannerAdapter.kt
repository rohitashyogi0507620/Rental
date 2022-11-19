package `in`.probusinsurance.app.Home.ProductData

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.mapwork.R
import com.example.mapwork.RoomItem
import com.smarteist.autoimageslider.SliderViewAdapter


class BannerAdapter(var context: Context, var mBannerHomeScreenItems: List<RoomItem>) : SliderViewAdapter<BannerAdapter.SliderAdapterVH>() {


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_room_full_view, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem: RoomItem = mBannerHomeScreenItems[position]
//        Glide.with(context).load(sliderItem.BannerUrl)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    viewHolder.progressBar.visibility = View.VISIBLE
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    viewHolder.progressBar.visibility = View.GONE
//                    return false
//                }
//            })
//            .into(viewHolder.imageViewBackground)
//

        viewHolder.imageViewBackground.setOnClickListener {
            Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCount(): Int {
        return mBannerHomeScreenItems.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        lateinit var itemView: View
       lateinit var imageViewBackground: ImageView
      lateinit  var progressBar: ProgressBar

        init {
         //   imageViewBackground = itemView.findViewById(R.id.banner_image)
          //  progressBar = itemView.findViewById(R.id.banner_progressBar)
        }
    }



}