package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import the.mrsmile.foodorderer.adapters.OrdersAdapter.OnOrderClick
import the.mrsmile.foodorderer.adapters.OrdersAdapter
import the.mrsmile.foodorderer.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.Toast
import the.mrsmile.foodorderer.models.RecommendedItems
import java.util.ArrayList

class OrderActivity : AppCompatActivity(), OnOrderClick {
    private var adapter: OrdersAdapter? = null
    private val list: MutableList<RecommendedItems> = ArrayList()
    var burger1id = R.drawable.lite_whooper_jr
    var burger2id = R.drawable.crispy_veg
    var burger3id = R.drawable.veg_makhani
    var burger4id = R.drawable.double_patty
    var burger5id = R.drawable.tikki_twist
    var burger1price = "Rs.109"
    var burger2price = "Rs.55"
    var burger3price = "Rs.60"
    var burger4price = "Rs.75"
    var burger5price = "Rs.60"
    var type: Int? = null

    //    public static final String Pizza = "Pizza";
    //    public static final String Chaamp = "Chaamp";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        type = intent.getIntExtra("TYPE",-1)
        val title = findViewById<TextView>(R.id.tvTest)
//        title.text = type
        when (type) {
            0 -> {
                val desc1 =
                    "Our Signature Whooper with 7 layers between the buns in convenient size."
                val desc2 =
                    "Our best-selling burger with crispy veg patty,fresh onion and our signature sauce."
                val desc3 =
                    "Enjoy rich makhani flavour with mix veg patty topped up with fresh onion."
                val desc4 =
                    "Our best-selling burger with crispy veg double patty,fresh onion and our signature sauce."
                val desc5 = "Tikki bhi,Twist bhi!! Our new signature burger with spicy sauce."
//                list.add(CategoryItems(burger1, desc1, burger1price, burger1id))
//                list.add(CategoryItems(burger2, desc2, burger2price, burger2id))
//                list.add(CategoryItems(burger3, desc3, burger3price, burger3id))
//                list.add(CategoryItems(burger4, desc4, burger4price, burger4id))
//                list.add(CategoryItems(burger5, desc5, burger5price, burger5id))
            }
            1 -> {
                val title1 = "Cheese Steamed Momos"
                val title2 = "Mix Veg Steamed Momos"
                val title3 = "Mix Veg Fried Momos"
                val title4 = "Cheese Fried Momos"
                val title5 = "Tandoori Gravy Momos"
                val desc1 = "Fine dough stuffed with cheese packed with our special sauce."
                val desc2 = "Veggies stuffed in fine dough and steamed."
                val desc3 = "Enjoy fried momos served with our signature sauce."
                val desc4 = "Our best-selling momos with cheese and fried for best-in-class taste."
                val desc5 = "Crunchy momos served with our signature sauce."
                val price1 = "Rs.50"
                val price2 = "Rs.45"
                val price3 = "Rs.50"
                val price4 = "Rs.70"
                val price5 = "Rs.60"
//                list.add(CategoryItems(title1, desc1, price1, R.drawable.cheese_setamed))
//                list.add(CategoryItems(title2, desc2, price2, R.drawable.mix_veg_momos))
//                list.add(CategoryItems(title3, desc3, price3, R.drawable.mix_veg_fried))
//                list.add(CategoryItems(title4, desc4, price4, R.drawable.cheese_fried))
//                list.add(CategoryItems(title5, desc5, price5, R.drawable.tandoori_gravy))
            }
            2 -> {

            }
        }
        adapter = OrdersAdapter(list, this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOrders)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onOrderClickk(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.orders_bottom_sheet)
        val name: TextView?
        val price: TextView?
        val total: TextView?
        val image: ImageView?
        val buy = bottomSheetDialog.findViewById<Button>(R.id.btnBuyBottomSheetOrders)
        name = bottomSheetDialog.findViewById(R.id.tvItemNameBottomSheetOrders)
        price = bottomSheetDialog.findViewById(R.id.tvItemPriceBottomSheetOrders)
        total = bottomSheetDialog.findViewById(R.id.tvTotalPriceBottomSheetOrders)
        image = bottomSheetDialog.findViewById(R.id.ivBottomSheetOrders)
        if (type == 0) {
            when (position) {
                0 -> {
                    assert(name != null)
                    assert(price != null)
                    assert(total != null)
                    assert(image != null)
                    name!!.text = burger1
                    price!!.text = burger1price
                    total!!.text = burger1price
                    image!!.setImageResource(burger1id)
                }
                1 -> {
                    assert(name != null)
                    assert(price != null)
                    assert(total != null)
                    assert(image != null)
                    name!!.text = burger2
                    price!!.text = burger2price
                    total!!.text = burger2price
                    image!!.setImageResource(burger2id)
                }
                2 -> {
                    assert(name != null)
                    assert(price != null)
                    assert(total != null)
                    assert(image != null)
                    name!!.text = burger3
                    price!!.text = burger3price
                    total!!.text = burger3price
                    image!!.setImageResource(burger3id)
                }
                3 -> {
                    assert(name != null)
                    assert(price != null)
                    assert(total != null)
                    assert(image != null)
                    name!!.text = burger4
                    price!!.text = burger4price
                    total!!.text = burger4price
                    image!!.setImageResource(burger4id)
                }
                4 -> {
                    assert(name != null)
                    assert(price != null)
                    assert(total != null)
                    assert(image != null)
                    name!!.text = burger5
                    price!!.text = burger5price
                    total!!.text = burger5price
                    image!!.setImageResource(burger5id)
                }
                else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            assert(buy != null)
            buy!!.setOnClickListener { view: View? ->
                Toast.makeText(
                    this,
                    "Order Placed Successfully !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            bottomSheetDialog.show()
        }
    }

    companion object {
        const val Burger = "Burger"
        const val Momos = "Momos"
        const val PaneerTikka = "Paneer Tikka"
        const val burger1 = "Lite Whopper Jr Veg"
        const val burger2 = "Crispy Veg Burger"
        const val burger3 = "Veg Makhani Burst Burger"
        const val burger4 = "Crispy Veg Double Patty Burger"
        const val burger5 = "Tikki Twist Burger"
    }
}