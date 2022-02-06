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
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    var flag1 = false
    var flag2 = false

    override fun onCreate(savedInstanceState: Bundle?) {

//        val splashScreen= installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
//        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
//            override fun onPreDraw(): Boolean {
//                return if(flag1 && flag2){
//                    binding.root.viewTreeObserver.removeOnPreDrawListener(this)
//                    true
//                } else{
//                    false
//                }
//            }
//
//        })
        setContentView(binding.root)
        hideStuff()

//        setupLocationPermissionStuff()


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

//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun setupLocationPermissionStuff() {
//        locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                    Toast.makeText(
//                        binding.root.context, "Granted precise ", Toast.LENGTH_SHORT
//                    ).show()
//                }
//                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                    Toast.makeText(
//                        binding.root.context, "Granted approx ", Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//                else -> {
//                    Toast.makeText(
//                        binding.root.context, "denied ", Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }

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
        flag1 = true
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
        val recyclerAdapter = RecyclerAdapter(list, this@MainActivity)
        flag2 = true
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
//        val currentLocation = bottomSheetDialog.findViewById<CardView>(R.id.cardView_bottomSheet)
//        currentLocation?.setOnClickListener {
//
//            when {
//                ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    Toast.makeText(
//                        binding.root.context, "Granted precise ", Toast.LENGTH_SHORT
//                    ).show()
//                }
//                else -> {
//                    requestPermission()
//                }
//            }
//
//
//        }
        bottomSheetDialog.show()
    }

//    private fun requestPermission() {
//
//        locationPermissionRequest.launch(
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        )
//    }

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
        Picasso.get().load(listRecommendedGlobal[position].image).into(image)


        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }

    private fun openOrderActivity(type: Int) {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
    }

    private fun hideStuff() {
        binding.svMainActivity.visibility = View.GONE

    }

    private fun showStuff() {
        binding.svMainActivity.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

}