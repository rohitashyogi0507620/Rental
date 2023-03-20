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
import com.example.mapwork.models.response.RecentSearch

class RecentSearchAdapter(
    val context: Context,
    val list: List<RecentSearch>,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(item: RecentSearch, position: Int, type: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recent_search, parent, false)
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

        holder.searchtitle.text = item.searchTitle
        holder.searchLocation.text = item.searchLocation
        holder.searchDistance.text = item.searchDistance


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchtitle = itemView.findViewById(R.id.recent_search_title) as TextView
        val searchLocation = itemView.findViewById(R.id.recent_search_location) as TextView
        val searchDistance = itemView.findViewById(R.id.recent_search_distance) as TextView

    }

    fun notifDataChanged() {
        notifyDataSetChanged()
    }

}