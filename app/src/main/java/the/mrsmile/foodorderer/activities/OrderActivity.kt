package the.mrsmile.foodorderer.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import the.mrsmile.foodorderer.adapters.OrdersAdapter.OnOrderClick
import the.mrsmile.foodorderer.adapters.OrdersAdapter
import the.mrsmile.foodorderer.R
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityOrderBinding
import the.mrsmile.foodorderer.models.OrderActivityItems
import java.util.ArrayList

class OrderActivity : AppCompatActivity(), OnOrderClick {

    private lateinit var binding: ActivityOrderBinding
    private var adapter: OrdersAdapter? = null
    private var type: Int? = null
    private lateinit var PATH: String
    private lateinit var dao: Dao
    private var listOrder : ArrayList<OrderActivityItems> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transparentStatusBar()
        type = intent.getIntExtra("TYPE", -1)
        if (type != -1) PATH = "${OrderActivityItems::class.java.simpleName}/$type"
        loadBigImage()
        getData()

    }

    override fun onOrderClickk(position: Int) {
        showOrderBottomSheet(position)
    }

    private fun getData() {
        dao = Dao(Firebase.database.getReference(PATH))
        val list: ArrayList<OrderActivityItems> = ArrayList()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(OrderActivityItems::class.java)
                        if (!list.contains(item) && item != null) {
                            list.add(item)
                        }
                    }
                    setRecyclerView(list)
                    listOrder = list
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrderActivity,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }

        }
        dao.get().addValueEventListener(valueEventListener)
    }

    private fun setRecyclerView(list: ArrayList<OrderActivityItems>) {

        adapter = OrdersAdapter(list, this, this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOrders)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.progressBarOrdersActivity.hide()
    }

    private fun loadBigImage() {
        Firebase.database.getReference("OrderActivityImages/$type/IMAGE")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue<String>()
                        if (item != null)
                            Glide.with(this@OrderActivity).load(item).centerCrop()
                                .into(binding.ivOrdersActivity)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(binding.root, "Something went wrong!", Snackbar.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun showOrderBottomSheet(position: Int){
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.orders_bottom_sheet)


        val title = bottomSheetDialog.findViewById<TextView>(R.id.tvItemNameBottomSheetOrders)
        val desc = bottomSheetDialog.findViewById<TextView>(R.id.tvItemDescBottomSheetOrders)
        val image = bottomSheetDialog.findViewById<ImageView>(R.id.ivBottomSheetOrders)
        val price = bottomSheetDialog.findViewById<TextView>(R.id.tvPriceBottomSheetOrders)
        val btnPlus = bottomSheetDialog.findViewById<ImageButton>(R.id.btnCounterPlusBottomSheet)
        val btnMinus = bottomSheetDialog.findViewById<ImageButton>(R.id.btnCounterMinusBottomSheet)
        val buy = bottomSheetDialog.findViewById<Button>(R.id.btnBuyBottomSheetOrders)
        val counter = bottomSheetDialog.findViewById<TextView>(R.id.tvCounterTextBottomSheet)
        var count = 1
        Glide.with(this).load(listOrder[position].image).placeholder(R.drawable.placeholder2).into(image!!)
        title?.text = listOrder[position].title
        desc?.text = listOrder[position].desc
        price?.text = listOrder[position].price

        btnPlus?.setOnClickListener {
            count++
            counter?.text = count.toString()
        }
        btnMinus?.setOnClickListener {
            if(count >1){
                count--
                counter?.text = count.toString()
            }
        }

        buy?.setOnClickListener {
            Toast.makeText(this,"Bought the ${listOrder[position].title}",Toast.LENGTH_SHORT).show()
        }
        bottomSheetDialog.show()
    }

    private fun transparentStatusBar(){
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }
}