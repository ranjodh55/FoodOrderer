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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityOrderBinding
import the.mrsmile.foodorderer.databinding.OrdersBottomSheetBinding
import the.mrsmile.foodorderer.models.BagItems
import the.mrsmile.foodorderer.models.OrderActivityItems
import java.util.ArrayList

class OrderActivity : AppCompatActivity(), OnOrderClick {

    private lateinit var binding: ActivityOrderBinding
    private var adapter: OrdersAdapter? = null
    private var type: Int? = null
    private lateinit var PATH: String
    private lateinit var dao: Dao
    private lateinit var daoBag: Dao
    private lateinit var auth: FirebaseAuth
    private var listOrder: ArrayList<OrderActivityItems> = ArrayList()
    private var listBagGlobal = ArrayList<BagItems>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transparentStatusBar()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        daoBag = Dao(
            Firebase.database.getReference(currentUser?.uid!!)
                .child(BagItems::class.java.simpleName)
        )

        type = intent.getIntExtra("TYPE", -1)
        if (type != -1) PATH = "${OrderActivityItems::class.java.simpleName}/$type"
        loadBigImage()
        getData()
        getBagItems()
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
                Snackbar.make(binding.root, "Something went wrong!", Snackbar.LENGTH_SHORT)
                    .show()
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
        Firebase.database.getReference("OrderActivityImages").child(type.toString()).child("IMAGE")
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

    private fun showOrderBottomSheet(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val sheetBinding = OrdersBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(sheetBinding.root)


        val title = sheetBinding.tvItemNameBottomSheetOrders
        val desc = sheetBinding.tvItemDescBottomSheetOrders
        val image = sheetBinding.ivBottomSheetOrders
        val price = sheetBinding.tvPriceBottomSheetOrders
        val btnPlus = sheetBinding.btnCounterPlusBottomSheet
        val btnMinus = sheetBinding.btnCounterMinusBottomSheet
        val buy = sheetBinding.btnBuyBottomSheetOrders
        val counter = sheetBinding.tvCounterTextBottomSheet
        var count = 1
        Glide.with(this).load(listOrder[position].image).placeholder(R.drawable.placeholder2)
            .into(image)
        title.text = listOrder[position].title
        desc.text = listOrder[position].desc
        price.text = listOrder[position].price.toString()

        btnPlus.setOnClickListener {
            count++
            counter.text = count.toString()
        }
        btnMinus.setOnClickListener {
            if (count > 1) {
                count--
                counter.text = count.toString()
            }
        }

        buy.setOnClickListener {
            val name = listOrder[position].title
            val pricee = listOrder[position].price
            val imagee = listOrder[position].image
            val time = listOrder[position].time
            val totalAmount = pricee?.times(count)
            val item = (BagItems(time, name, count, imagee, totalAmount))

            var flag = false
            var pos = 0
            if (listBagGlobal.isNotEmpty()) {
                for (i in 0 until listBagGlobal.size) {
                    if (listBagGlobal[i].name == item.name) {
                        flag = true
                        pos = i
                    }
                }
                if (flag) {
                    val quantityNew =
                        item.quantity?.let { it1 ->
                            listBagGlobal[pos].quantity?.plus(it1)
                        }
                    val priceNew = item.price?.let { it2 ->
                        listBagGlobal[pos].price?.plus(it2)
                    }
                    item.quantity = quantityNew
                    val hashMap = HashMap<String, Any>()
                    val key = listBagGlobal[pos].key
                    if (quantityNew != null && priceNew != null) {
                        hashMap["quantity"] = quantityNew
                        hashMap["price"] = priceNew
                    }
                    if (key != null) {
                        daoBag.update(key, hashMap).addOnCompleteListener {
                            if (it.isSuccessful) {
                                showToast()
                            }
                        }
                    }
                } else {
                    daoBag.addBagItem(item).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast()
                        }
                    }
                }

            } else if (listBagGlobal.isNullOrEmpty()) {
                daoBag.addBagItem(item).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast()
                    }
                }
            }
            bottomSheetDialog.dismissWithAnimation = true
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun transparentStatusBar() {
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun getBagItems() {
        val list = ArrayList<BagItems>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(BagItems::class.java)
                        if (dataSnapshot.key != null) {
                            item?.key = dataSnapshot.key
                        }
                        if (item != null) {
                            list.add(item)
                        }
                    }
                    listBagGlobal = list
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        daoBag.get().addValueEventListener(valueEventListener)
    }

    private fun showToast() {
        val view =
            layoutInflater.inflate(R.layout.custom_toast, findViewById(R.id.llCustomToast), false)
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }
}