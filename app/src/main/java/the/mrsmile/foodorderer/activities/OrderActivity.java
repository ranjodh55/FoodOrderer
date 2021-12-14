package the.mrsmile.foodorderer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import the.mrsmile.foodorderer.R;
import the.mrsmile.foodorderer.adapters.OrdersAdapter;
import the.mrsmile.foodorderer.models.CategoryItems;
import the.mrsmile.foodorderer.models.OrderItems;

public class OrderActivity extends AppCompatActivity implements OrdersAdapter.onOrderClick {

    private OrdersAdapter adapter;
    private final List<OrderItems> list = new ArrayList<>();
    public static final String Burger = "Burger";
    public static final String Momos = "Momos";
    public static final String PaneerTikka = "Paneer Tikka";
    public static final String burger1 = "Lite Whopper Jr Veg";
    public static final String burger2 = "Crispy Veg Burger";
    public static final String burger3 = "Veg Makhani Burst Burger";
    public static final String burger4 = "Crispy Veg Double Patty Burger";
    public static final String burger5 = "Tikki Twist Burger";
    int burger1id = R.drawable.lite_whooper_jr;
    int burger2id = R.drawable.crispy_veg;
    int burger3id = R.drawable.veg_makhani;
    int burger4id = R.drawable.double_patty;
    int burger5id = R.drawable.tikki_twist;
    String burger1price = "Rs.109";
    String burger2price = "Rs.55";
    String burger3price = "Rs.60";
    String burger4price = "Rs.75";
    String burger5price = "Rs.60";
//    public static final String Pizza = "Pizza";
//    public static final String Chaamp = "Chaamp";

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
                String desc1 = "Our Signature Whooper with 7 layers between the buns in convenient size.";
                String desc2 = "Our best-selling burger with crispy veg patty,fresh onion and our signature sauce.";
                String desc3 = "Enjoy rich makhani flavour with mix veg patty topped up with fresh onion.";
                String desc4 = "Our best-selling burger with crispy veg double patty,fresh onion and our signature sauce.";
                String desc5 = "Tikki bhi,Twist bhi!! Our new signature burger with spicy sauce.";

                list.add(new OrderItems(burger1, desc1, burger1price, burger1id));
                list.add(new OrderItems(burger2, desc2, burger2price, burger2id));
                list.add(new OrderItems(burger3, desc3, burger3price, burger3id));
                list.add(new OrderItems(burger4, desc4, burger4price, burger4id));
                list.add(new OrderItems(burger5, desc5, burger5price, burger5id));
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
        adapter = new OrdersAdapter(list,this);

        RecyclerView recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onOrderClickk(int position) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.orders_bottom_sheet);

        TextView name , price , total ;
        ImageView image;
        Button buy = bottomSheetDialog.findViewById(R.id.btnBuyBottomSheetOrders);
        name = bottomSheetDialog.findViewById(R.id.tvItemNameBottomSheetOrders);
        price = bottomSheetDialog.findViewById(R.id.tvItemPriceBottomSheetOrders);
        total = bottomSheetDialog.findViewById(R.id.tvTotalPriceBottomSheetOrders);
        image= bottomSheetDialog.findViewById(R.id.ivBottomSheetOrders);

        switch (position) {
            case 0 : {
                assert name != null;
                assert price != null;
                assert total != null;
                assert image != null;
                name.setText(burger1);
                price.setText(burger1price);
                total.setText(burger1price);
                image.setImageResource(burger1id);
                break;
            }
            case 1 : {
                assert name != null;
                assert price != null;
                assert total != null;
                assert image != null;
                name.setText(burger2);
                price.setText(burger2price);
                total.setText(burger2price);
                image.setImageResource(burger2id);
                break;
            }
            case 2 : {
                assert name != null;
                assert price != null;
                assert total != null;
                assert image != null;
                name.setText(burger3);
                price.setText(burger3price);
                total.setText(burger3price);
                image.setImageResource(burger3id);
                break;
            }
            case 3 : {
                assert name != null;
                assert price != null;
                assert total != null;
                assert image != null;
                name.setText(burger4);
                price.setText(burger4price);
                total.setText(burger4price);
                image.setImageResource(burger4id);
                break;
            }
            case 4 : {
                assert name != null;
                assert price != null;
                assert total != null;
                assert image != null;
                name.setText(burger5);
                price.setText(burger5price);
                total.setText(burger5price);
                image.setImageResource(burger5id);
                break;
            }
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        assert buy != null;
        buy.setOnClickListener(view -> {
            Toast.makeText(this, "Order Placed Successfully !", Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialog.show();
    }
}