package the.mrsmile.foodorderer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import the.mrsmile.foodorderer.R;
import the.mrsmile.foodorderer.models.CategoryItems;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    List<CategoryItems> list;
    onClick listener;
    public RecyclerAdapter(List<CategoryItems> list,onClick listener) {
        this.list = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        CategoryItems currentItem = list.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDesc());
        holder.price.setText(currentItem.getPrice());
        holder.imageView.setImageResource(currentItem.getImage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView title, desc, price;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.tvTitleRecyclerMain);
            desc = itemView.findViewById(R.id.tvDescRecyclerMain);
            price = itemView.findViewById(R.id.tvPriceRecyclerMain);
        }

        {
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public interface onClick {
        void onItemClick(int position);
    }
}


