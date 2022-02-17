package the.mrsmile.foodorderer.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.activities.MainActivity
import the.mrsmile.foodorderer.models.CategoryItems
import java.lang.Exception

class ViewPagerAdapter(private var list: List<CategoryItems>, var listener: OnClickInterface,val context: Context) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_pager_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (!list[position].image.isNullOrEmpty())
            Glide.with(context).load(currentItem.image).placeholder(R.drawable.placeholder2).centerCrop().into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var image: ImageView = itemView.findViewById(R.id.ivViewPager)

        //implemented method of View.OnClickListener interface
        override fun onClick(view: View) {
            listener.onCategoriesClick(absoluteAdapterPosition)
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