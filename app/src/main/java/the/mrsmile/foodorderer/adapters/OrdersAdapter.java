package the.mrsmile.foodorderer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import the.mrsmile.foodorderer.R;
import the.mrsmile.foodorderer.models.OrderItems;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    List<OrderItems> list;
    onOrderClick listener;
    public OrdersAdapter(List<OrderItems> list,onOrderClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_orders_items, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        OrderItems currentItem = list.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDesc());
        holder.price.setText(currentItem.getPrice());
        holder.image.setImageResource(currentItem.getImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, price;
        ImageView image;
        Button btn;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitleOrders);
            desc = itemView.findViewById(R.id.tvDescOrders);
            price = itemView.findViewById(R.id.tvPriceOrders);
            image = itemView.findViewById(R.id.ivRecyclerOrders);
            btn = itemView.findViewById(R.id.btnAddToCart);

            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onOrderClickk(getAdapterPosition());
        }
    }

    public interface onOrderClick{
        void onOrderClickk(int position);
    }
}
