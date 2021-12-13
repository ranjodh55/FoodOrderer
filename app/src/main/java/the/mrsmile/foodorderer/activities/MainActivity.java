package the.mrsmile.foodorderer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import the.mrsmile.foodorderer.R;
import the.mrsmile.foodorderer.adapters.RecyclerAdapter;
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter;
import the.mrsmile.foodorderer.databinding.ActivityMainBinding;
import the.mrsmile.foodorderer.models.CategoryItems;

public class MainActivity extends AppCompatActivity implements ViewPagerAdapter.OnClickInterface, RecyclerAdapter.onClick {

    private ViewPager2 viewPager;
    private final List<Integer> list = new ArrayList<>();
    private final List<CategoryItems> listRecycler = new ArrayList<>();
    private ActivityMainBinding binding;
    Toolbar toolbar;
    private ViewPagerAdapter adapter;
    public static final String CATEGORIES = "CATEGORIES";
    public static final String RECOMMENDED = "RECOMMENDED";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.ivFastDelivery.setClipToOutline(true);
        viewPager = binding.viewPager;
        adapter = new ViewPagerAdapter(list, this);
        toolbar = binding.toolBar;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        ImageView profile = binding.ivProfile;
        profile.setOnClickListener(view -> Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show());

        TextView address = binding.tvAddress;
        address.setOnClickListener(view -> onAddressClick());

        ImageView addressArrow = binding.ivAddressArrow;
        addressArrow.setOnClickListener(view -> onAddressClick());


        list.add(R.drawable.burger);
        list.add(R.drawable.momos2);
        list.add(R.drawable.paneertikka2);
        list.add(R.drawable.pizza);
        list.add(R.drawable.chaampp);


        listRecycler.add(new CategoryItems("Cheesy Pizza", "Italian", "Rs.150 for one", R.drawable.pizza_));
        listRecycler.add(new CategoryItems("Delicious Ice Cream", "Dessert", "Rs.80 for one", R.drawable.icecream_));
        listRecycler.add(new CategoryItems("Creamy Burger", "Street Food", "Rs.70 for one", R.drawable.lite_whooper_jr));
        listRecycler.add(new CategoryItems("Masala Dosa", "South Indian", "Rs.100 for one", R.drawable.dosa_));
        listRecycler.add(new CategoryItems("Crunchy Samosa", "North Indian", "Rs.70 for one", R.drawable.samosa_));

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = binding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Burger ");
                    } else if (position == 1) {
                        tab.setText("Momos ");
                    } else if (position == 2) {
                        tab.setText("Paneer Tikka ");
                    } else if (position == 3) {
                        tab.setText("Pizza ");
                    } else if (position == 4) {
                        tab.setText("Chaamp ");
                    } else {
                        tab.setText("Position" + position);
                    }
                }).attach();

        RecyclerView recyclerView = binding.recyclerRecommended;

        RecyclerAdapter adapter1 = new RecyclerAdapter(listRecycler, this);
        recyclerView.setAdapter(adapter1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCategoriesClick(int position) {
        switch (position) {
            case 0: {
                openOrderActivity("Burger");
                break;
            }
            case 1: {
                openOrderActivity("Momos");
                break;
            }
            case 2: {
                openOrderActivity("Paneer Tikka");
                break;
            }
            case 3: {
                openOrderActivity("Pizza");
                break;
            }
            case 4: {
                openOrderActivity("Chaamp");
                break;
            }

        }

    }

    public void onAddressClick() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

        CardView currentLocation = bottomSheetDialog.findViewById(R.id.cardView_bottomSheet);

        if (currentLocation != null) {
            currentLocation.setOnClickListener(view -> Toast.makeText(binding.getRoot().getContext(), "Clicked", Toast.LENGTH_SHORT).show());
        }
        bottomSheetDialog.show();

    }

    @Override
    public void onRecommendedItemClick(int position) {
        openOrderActivity(RECOMMENDED);
    }

    private void openOrderActivity(String type) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);

    }
}
