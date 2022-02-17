package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter.OnClickInterface
import the.mrsmile.foodorderer.adapters.RecyclerAdapter.OnRecommendedClick
import androidx.viewpager2.widget.ViewPager2
import the.mrsmile.foodorderer.models.RecommendedItems
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter
import android.os.Bundle
import android.widget.Toast
import the.mrsmile.foodorderer.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import the.mrsmile.foodorderer.adapters.RecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityMainBinding
import the.mrsmile.foodorderer.models.CategoryItems
import java.util.ArrayList

class MainActivity : AppCompatActivity(), OnClickInterface, OnRecommendedClick {
    private var viewPager: ViewPager2? = null
    private var listRecommendedGlobal: ArrayList<RecommendedItems> = ArrayList()
    private var listCategoryGlobal: ArrayList<CategoryItems> = ArrayList()
    private lateinit var binding: ActivityMainBinding
    private var toolbar: Toolbar? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var daoRecommended: Dao
    private lateinit var daoCategory: Dao
    private val log = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        hideStuff()

        val profile = binding.ivmenu
        val address = binding.clAddressMainActivity

        toolbar = binding.toolBar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)

        profile.setOnClickListener {
            Toast.makeText(
                this,
                "Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        address.setOnClickListener {
            onAddressClick()
        }


        getRecommendedItemsData()
        getCategoryItemData()

    }

    private fun setViewPager(list: ArrayList<CategoryItems>) {

        viewPager = binding.viewPager
        viewPagerAdapter = ViewPagerAdapter(list, this, this)
        Log.e(log, listCategoryGlobal.toString())
        viewPager!!.adapter = viewPagerAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(
            tabLayout, viewPager!!
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = list[position].tabTitle
                }
                1 -> {
                    tab.text = list[position].tabTitle
                }
                2 -> {
                    tab.text = list[position].tabTitle
                }
                3 -> {
                    tab.text = list[position].tabTitle
                }
                4 -> {
                    tab.text = list[position].tabTitle
                }
                else -> {
                    tab.text = "Position$position"
                }
            }
        }.attach()
        listCategoryGlobal = list
        showStuff()
    }

    private fun getCategoryItemData() {

        val list = ArrayList<CategoryItems>()
        daoCategory = Dao(Firebase.database.getReference(CategoryItems::class.java.simpleName))
        daoCategory.get().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(CategoryItems::class.java)
                        if (item != null && !list.contains(item)) {
                            list.add(item)
                        }
                    }
                setViewPager(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Error loading items", Snackbar.LENGTH_LONG).show()
            }

        })
    }

    private fun getRecommendedItemsData() {

        val list = ArrayList<RecommendedItems>()
        daoRecommended =
            Dao(Firebase.database.getReference(RecommendedItems::class.java.simpleName))
        daoRecommended.get().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(RecommendedItems::class.java)

                    if (item != null && !list.contains(item)) {
                        list.add(item)
                    }
                }
                setRecyclerRecommended(list)
                listRecommendedGlobal = list
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Error loading items", Snackbar.LENGTH_LONG).show()
            }

        })
    }

    private fun setRecyclerRecommended(list: ArrayList<RecommendedItems>) {
        val recyclerView = binding.recyclerRecommended
        val recyclerAdapter = RecyclerAdapter(list, this@MainActivity, this)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    override fun onCategoriesClick(position: Int) {
        when (position) {
            0 -> {
                openOrderActivity(0)
            }
            1 -> {
                openOrderActivity(1)
            }
            2 -> {
                openOrderActivity(2)
            }
            3 -> {
                openOrderActivity(3)
            }
            4 -> {
                openOrderActivity(4)
            }
        }
    }

    private fun onAddressClick() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.address_bottom_sheet_layout)
        bottomSheetDialog.show()
    }

    override fun onRecommendedItemClick(position: Int) {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.orders_bottom_sheet)

        var count = 1
        val image = bottomSheetDialog.findViewById<ImageView>(R.id.ivBottomSheetOrders)
        val itemName = bottomSheetDialog.findViewById<TextView>(R.id.tvItemNameBottomSheetOrders)
        val itemPrice = bottomSheetDialog.findViewById<TextView>(R.id.tvPriceBottomSheetOrders)
        val counterDecrease =
            bottomSheetDialog.findViewById<ImageButton>(R.id.btnCounterMinusBottomSheet)
        val counterText = bottomSheetDialog.findViewById<TextView>(R.id.tvCounterTextBottomSheet)
        val counterIncrease =
            bottomSheetDialog.findViewById<ImageButton>(R.id.btnCounterPlusBottomSheet)
        val itemDesc = bottomSheetDialog.findViewById<TextView>(R.id.tvItemDescBottomSheetOrders)

        val rating = bottomSheetDialog.findViewById<TextView>(R.id.tvRatingOrdersBottomSHeet)
        val calories = bottomSheetDialog.findViewById<TextView>(R.id.tvCaloriesOrdersBottomSheet)
        val time = bottomSheetDialog.findViewById<TextView>(R.id.tvTimeOrdersBottomSheet)


        rating?.text = listRecommendedGlobal[position].rating
        calories?.text = listRecommendedGlobal[position].calories
        time?.text = listRecommendedGlobal[position].time


        itemName?.text = listRecommendedGlobal[position].title
        itemPrice?.text = listRecommendedGlobal[position].price
        itemDesc?.text = listRecommendedGlobal[position].desc

        counterDecrease?.setOnClickListener {

            if (count > 1) {
                count--
                counterText?.text = count.toString()

            }
        }

        counterIncrease?.setOnClickListener {
            count++
            counterText?.text = count.toString()
        }


        Glide.with(this).load(listRecommendedGlobal[position].image)
            .placeholder(R.drawable.placeholder2).into(image!!)

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }

    private fun openOrderActivity(type: Int) {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
    }

    private fun hideStuff() {
        binding.svMainActivity.visibility = View.INVISIBLE
    }

    fun showStuff() {
        binding.svMainActivity.visibility = View.VISIBLE
        binding.progressBar.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val auth = Firebase.auth
        when (item.itemId) {
            R.id.signOut -> {
                auth.signOut()
                if (auth.currentUser == null) {
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    Intent(this, LoginActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
        }

        return true
    }
}