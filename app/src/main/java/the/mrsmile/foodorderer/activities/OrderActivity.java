package the.mrsmile.foodorderer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import the.mrsmile.foodorderer.R;
import the.mrsmile.foodorderer.adapters.OrdersAdapter;
import the.mrsmile.foodorderer.models.CategoryItems;
import the.mrsmile.foodorderer.models.OrderItems;

public class OrderActivity extends AppCompatActivity {

    private OrdersAdapter adapter;
    private final List<OrderItems> list = new ArrayList<>();
    public static final String Burger = "Burger";
    public static final String Momos = "Momos";
    public static final String PaneerTikka = "Paneer Tikka";
    public static final String Pizza = "Pizza";
    public static final String Chaamp = "Chaamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        String type = intent.getStringExtra("TYPE");

        TextView title = findViewById(R.id.tvTest);
        title.setText(type);

        switch (type) {
            case Burger: {
                String title1 = "Lite Whopper Jr Veg";
                String title2 = "Crispy Veg Burger";
                String title3 = "Veg Makhani Burst Burger";
                String title4 = "Crispy Veg Double Patty Burger";
                String title5 = "Tikki Twist Burger";
                String desc1 = "Our Signature Whooper with 7 layers between the buns in convenient size.";
                String desc2 = "Our best-selling burger with crispy veg patty,fresh onion and our signature sauce.";
                String desc3 = "Enjoy rich makhani flavour with mix veg patty topped up with fresh onion.";
                String desc4 = "Our best-selling burger with crispy veg double patty,fresh onion and our signature sauce.";
                String desc5 = "Tikki bhi,Twist bhi!! Our new signature burger with spicy sauce.";
                String price1 = "Rs.109";
                String price2 = "Rs.55";
                String price3 = "Rs.60";
                String price4 = "Rs.75";
                String price5 = "Rs.60";
                list.add(new OrderItems(title1, desc1, price1, R.drawable.lite_whooper_jr));
                list.add(new OrderItems(title2, desc2, price2, R.drawable.crispy_veg));
                list.add(new OrderItems(title3, desc3, price3, R.drawable.veg_makhani));
                list.add(new OrderItems(title4, desc4, price4, R.drawable.double_patty));
                list.add(new OrderItems(title5, desc5, price5, R.drawable.tikki_twist));
                break;
            }

            case Momos: {
                String title1 = "Cheese Steamed Momos";
                String title2 = "Mix Veg Steamed Momos";
                String title3 = "Mix Veg Fried Momos";
                String title4 = "Cheese Fried Momos";
                String title5 = "Tandoori Gravy Momos";
                String desc1 = "Fine dough stuffed with cheese packed with our special sauce.";
                String desc2 = "Veggies stuffed in fine dough and steamed.";
                String desc3 = "Enjoy fried momos served with our signature sauce.";
                String desc4 = "Our best-selling momos with cheese and fried for best-in-class taste.";
                String desc5 = "Crunchy momos served with our signature sauce.";
                String price1 = "Rs.50";
                String price2 = "Rs.45";
                String price3 = "Rs.50";
                String price4 = "Rs.70";
                String price5 = "Rs.60";
                list.add(new OrderItems(title1, desc1, price1, R.drawable.cheese_setamed));
                list.add(new OrderItems(title2, desc2, price2, R.drawable.mix_veg_momos));
                list.add(new OrderItems(title3, desc3, price3, R.drawable.mix_veg_fried));
                list.add(new OrderItems(title4, desc4, price4, R.drawable.cheese_fried));
                list.add(new OrderItems(title5, desc5, price5, R.drawable.tandoori_gravy));
                break;
            }

            case PaneerTikka: {

            }
        }
        adapter = new OrdersAdapter(list);

        RecyclerView recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}