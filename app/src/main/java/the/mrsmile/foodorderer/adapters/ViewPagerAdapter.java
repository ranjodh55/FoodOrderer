package the.mrsmile.foodorderer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import the.mrsmile.foodorderer.R;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    List<Integer> list;
    OnClickInterface listener;

    public ViewPagerAdapter(List<Integer> list, OnClickInterface listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentItem = list.get(position);
        holder.image.setImageResource(currentItem);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivViewPager);

//            image.setClipToOutline(true);
        }

        //init block
        {
            itemView.setOnClickListener(this);
        }


        //implemented method of View.OnClickListener interface
        @Override
        public void onClick(View view) {
            listener.onCategoriesClick(getAdapterPosition());
        }
    }

    //created a new interface to control on Click events from main activity
    public interface OnClickInterface {
        void onCategoriesClick(int position);
    }
}

