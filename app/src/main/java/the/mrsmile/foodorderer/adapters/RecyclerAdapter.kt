package the.mrsmile.foodorderer.adapters

import android.content.Context
import android.util.Log
import the.mrsmile.foodorderer.models.RecommendedItems
import androidx.recyclerview.widget.RecyclerView
import the.mrsmile.foodorderer.adapters.RecyclerAdapter.RecyclerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView import the.mrsmile.foodorderer.R
import android.widget.TextView
import com.bumptech.glide.Glide

class RecyclerAdapter(
    private var list: List<RecommendedItems>,
    var listener: OnRecommendedClick,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_main, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentItem = list[position]
        holder.title.text = currentItem.title
        holder.desc.text = currentItem.desc
        holder.price.text = currentItem.price.toString()
        if (!list[position].image.isNullOrEmpty())
        Glide.with(context).load(currentItem.image).placeholder(R.drawable.placeholder2).dontAnimate().centerCrop().into(holder.image)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val image: ImageView = itemView.findViewById(R.id.iv_home_ad)
        val title: TextView = itemView.findViewById(R.id.tvTitleRecyclerMain)
        val desc: TextView = itemView.findViewById(R.id.tvDescRecyclerMain)
        val price: TextView = itemView.findViewById(R.id.tvPriceRecyclerMain)

        override fun onClick(view: View) {
            listener.onRecommendedItemClick(absoluteAdapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface OnRecommendedClick {
        fun onRecommendedItemClick(position: Int)
    }
}