package the.mrsmile.foodorderer.adapters

import the.mrsmile.foodorderer.models.RecommendedItems
import androidx.recyclerview.widget.RecyclerView
import the.mrsmile.foodorderer.adapters.RecyclerAdapter.RecyclerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import the.mrsmile.foodorderer.R
import android.widget.TextView
import com.squareup.picasso.Picasso

class RecyclerAdapter(var list: List<RecommendedItems>, var listener: onClick) :
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
        Picasso.get().load(currentItem.image).into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var image: ImageView
        var title: TextView
        var desc: TextView
        var price: TextView
        override fun onClick(view: View) {
            listener.onRecommendedItemClick(adapterPosition)
        }

        init {
            image = itemView.findViewById(R.id.iv_home_ad)
            title = itemView.findViewById(R.id.tvTitleRecyclerMain)
            desc = itemView.findViewById(R.id.tvDescRecyclerMain)
            price = itemView.findViewById(R.id.tvPriceRecyclerMain)
            itemView.setOnClickListener(this)

        }
    }

    interface onClick {
        fun onRecommendedItemClick(position: Int)
    }
}