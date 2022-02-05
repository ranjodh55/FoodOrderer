package the.mrsmile.foodorderer.adapters

import android.graphics.Bitmap
import the.mrsmile.foodorderer.models.RecommendedItems
import androidx.recyclerview.widget.RecyclerView
import the.mrsmile.foodorderer.adapters.RecyclerAdapter.RecyclerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import the.mrsmile.foodorderer.R
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class RecyclerAdapter(private var list: List<RecommendedItems>, var listener: OnRecommendedClick) :
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
        holder.price.text = currentItem.price
        holder.image.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.picasso.load(currentItem.image).into(holder.image , object : Callback{
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {

            }

        })
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
        val picasso : Picasso
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_recyclerMain)

        override fun onClick(view: View) {
            listener.onRecommendedItemClick(absoluteAdapterPosition)
        }

        init {
            picasso = Picasso.get()
            itemView.setOnClickListener(this)

        }
    }

    interface OnRecommendedClick {
        fun onRecommendedItemClick(position: Int)
    }
}