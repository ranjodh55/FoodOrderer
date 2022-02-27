package the.mrsmile.foodorderer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.databinding.RecyclerViewBagBinding
import the.mrsmile.foodorderer.models.BagItems

class BagRecyclerAdapter(
    private val list: ArrayList<BagItems>,
    private val context: Context,
    val listener: Click
) : RecyclerView.Adapter<BagRecyclerAdapter.BagViewHolder>() {

    inner class BagViewHolder(binding: RecyclerViewBagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivRecyclerBag
        val price = binding.tvPriceBag
        val quantity = binding.tvQuantityBag
        val name = binding.tvTitleBag

        init {
            binding.btnCounterMinusBag.setOnClickListener {
                listener.onDecButtonClick(it,absoluteAdapterPosition)
            }
            binding.btnCounterPlusBag.setOnClickListener {
                listener.onIncButtonClick(it,absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val binding = RecyclerViewBagBinding.inflate(LayoutInflater.from(parent.context))
        return BagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        val currentItem = list[position]
        holder.name.text = currentItem.name
        holder.price.text = currentItem.price.toString()
        holder.quantity.text = currentItem.quantity.toString()

        Glide.with(context).load(currentItem.image).placeholder(R.drawable.placeholder2)
            .into(holder.image)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Click {
        fun onDecButtonClick(view: View, position: Int)
        fun onIncButtonClick(view: View, position: Int)
    }
}