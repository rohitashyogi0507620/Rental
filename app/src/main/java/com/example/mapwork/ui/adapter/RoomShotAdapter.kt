import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mapwork.R
import com.example.mapwork.databinding.LayoutRoominfoShortBinding
import com.example.mapwork.models.response.RoomData

class RoomShotAdapter(
    val context: Context,
    val list: List<RoomData>,
    val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RoomShotAdapter.NoteViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(item: RoomData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            LayoutRoominfoShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = list.get(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class NoteViewHolder(private val binding: LayoutRoominfoShortBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(roomItem: RoomData) {

            binding.roomTxttitle.setText(roomItem.title)
            binding.roomTxtlocation.setText(roomItem.location)
            binding.roomPricebtn.setText(roomItem.price)
            Glide.with(context).load(roomItem.image)
                .thumbnail(Glide.with(context).load(R.drawable.ic_room))
                .into(binding.roomImage)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(roomItem)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size;
    }

    fun notifDataChanged(){
        notifyDataSetChanged()
    }


}