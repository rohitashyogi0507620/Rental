import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mapwork.R
import com.example.mapwork.api.APIConstant.BASE_URL
import com.example.mapwork.databinding.LayoutRoominfoShortBinding
import com.example.mapwork.models.response.Image
import com.example.mapwork.models.response.PropertyDataResponse

class RoomShotAdapter(
    val context: Context,
    val list: List<PropertyDataResponse>,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RoomShotAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(item: PropertyDataResponse, position: Int, type: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_roominfo_short, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var item = list.get(position)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item, position, 0)
        }
        holder.roomDirection.setOnClickListener {
            itemClickListener.onItemClick(item, position, 1)
        }

        holder.roomTitle.text = item.property_type.capitalize()
        holder.roomlLcation.text = "${item.plot_number} , ${item.street} ${item.landmark} ${item.city} ${item.pincode}"
        holder.roomPrice.text = context.getString(R.string.rupesssign) + " ${item.price}" + context.getString(R.string.rupesending)

        if (!item.images.isNullOrEmpty() && item.images.size > 0) {
            var imageUrl = BASE_URL + item.images.get(0).image
            Log.d("IMAGEURL", imageUrl)
            Glide.with(context)
                .load(imageUrl)
                .into(holder.roomImage)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val roomTitle = itemView.findViewById(R.id.room_txttitle) as TextView
        val roomlLcation = itemView.findViewById(R.id.room_txtlocation) as TextView
        val roomPrice = itemView.findViewById(R.id.room_txtprice) as TextView
        val roomImage = itemView.findViewById(R.id.room_image) as ImageView
        val roomDirection = itemView.findViewById(R.id.room_directionicon) as ImageView

    }

    fun notifDataChanged() {
        notifyDataSetChanged()
    }

}