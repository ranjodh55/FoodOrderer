package the.mrsmile.foodorderer.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import the.mrsmile.foodorderer.R

class ViewPagerAdapter(private var list: List<Int>, var listener: OnClickInterface) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_pager_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.image.setImageResource(currentItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var image: ImageView = itemView.findViewById(R.id.ivViewPager)

        //implemented method of View.OnClickListener interface
        override fun onClick(view: View) {
            listener.onCategoriesClick(adapterPosition)
        }

        //init block
        init {
            itemView.setOnClickListener(this)
        }
    }

    //created a new interface to control on Click events from main activity
    interface OnClickInterface {
        fun onCategoriesClick(position: Int)
    }
}