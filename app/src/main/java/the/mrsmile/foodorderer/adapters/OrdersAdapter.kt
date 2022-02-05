package the.mrsmile.foodorderer.adapters

import androidx.recyclerview.widget.RecyclerView
import the.mrsmile.foodorderer.adapters.OrdersAdapter.OrdersViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import the.mrsmile.foodorderer.R
import android.widget.TextView
import com.squareup.picasso.Picasso
import the.mrsmile.foodorderer.models.RecommendedItems

class OrdersAdapter(var list: List<RecommendedItems>, var listener: OnOrderClick) :
    RecyclerView.Adapter<OrdersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_orders_items, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val currentItem = list[position]
        holder.title.text = currentItem.title
        holder.desc.text = currentItem.desc
        holder.price.text = currentItem.price
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var title: TextView = itemView.findViewById(R.id.tvTitleOrders)
        var desc: TextView = itemView.findViewById(R.id.tvDescOrders)
        var price: TextView = itemView.findViewById(R.id.tvPriceOrders)
        var image: ImageView = itemView.findViewById(R.id.ivRecyclerOrders)
        var btn: Button = itemView.findViewById(R.id.btnAddToCart)
        override fun onClick(view: View) {
            listener.onOrderClickk(adapterPosition)
        }

        init {
            btn.setOnClickListener(this)
        }
    }

    interface OnOrderClick {
        fun onOrderClickk(position: Int)
    }
}