package the.mrsmile.foodorderer.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import the.mrsmile.foodorderer.R
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
        holder.picasso.load(currentItem.image).into(holder.image,object : Callback{
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var image: ImageView = itemView.findViewById(R.id.ivViewPager)
        val picasso = Picasso.get()
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar_viewPager)

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