package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import the.mrsmile.foodorderer.adapters.OrdersAdapter.OnOrderClick
import the.mrsmile.foodorderer.adapters.OrdersAdapter
import the.mrsmile.foodorderer.R
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import the.mrsmile.foodorderer.models.RecommendedItems
import java.util.ArrayList

class OrderActivity : AppCompatActivity(), OnOrderClick {
    private var adapter: OrdersAdapter? = null
    private val list: MutableList<RecommendedItems> = ArrayList()
    private var type: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        type = intent.getIntExtra("TYPE", -1)

        adapter = OrdersAdapter(list, this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOrders)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onOrderClickk(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.orders_bottom_sheet)

    }
}