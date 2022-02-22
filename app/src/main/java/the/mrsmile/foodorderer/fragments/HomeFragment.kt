package the.mrsmile.foodorderer.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.activities.LoginActivity
import the.mrsmile.foodorderer.activities.OrderActivity
import the.mrsmile.foodorderer.adapters.RecyclerAdapter
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.FragmentHomeBinding
import the.mrsmile.foodorderer.models.BagItems
import the.mrsmile.foodorderer.models.CategoryItems
import the.mrsmile.foodorderer.models.RecommendedItems


class HomeFragment : Fragment(), ViewPagerAdapter.OnClickInterface,
    RecyclerAdapter.OnRecommendedClick {

    private lateinit var binding: FragmentHomeBinding
    private var viewPager: ViewPager2? = null
    private var listRecommendedGlobal: ArrayList<RecommendedItems> = ArrayList()
    private var listCategoryGlobal: ArrayList<CategoryItems> = ArrayList()
    private var listBagGlobal: ArrayList<BagItems> = ArrayList()
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var daoRecommended: Dao
    private lateinit var daoCategory: Dao
    private val log = "MainActivity"
    private lateinit var daoBag: Dao
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initAllDao()
        hideStuff()

        toolbar = binding.toolBar
        toolbar.title = ""
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)


        if (listCategoryGlobal.isEmpty() && listRecommendedGlobal.isEmpty()) {
            getBagData()
            getRecommendedItemsData()
            getCategoryItemData()

        } else {
            setViewPager(listCategoryGlobal)
            setRecyclerRecommended(listRecommendedGlobal)
            showStuff()
        }

        return binding.root
    }

    private fun setViewPager(list: ArrayList<CategoryItems>) {

        viewPager = binding.viewPager
        viewPagerAdapter = ViewPagerAdapter(list, this, requireContext())
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
        val recyclerAdapter = RecyclerAdapter(list, this, requireContext())
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.address_bottom_sheet_layout)
        bottomSheetDialog.show()
    }

    override fun onRecommendedItemClick(position: Int) {

        val bottomSheetDialog = BottomSheetDialog(requireContext())
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
        val btnAddToBag =
            bottomSheetDialog.findViewById<MaterialButton>(R.id.btnBuyBottomSheetOrders)


        rating?.text = listRecommendedGlobal[position].rating
        calories?.text = listRecommendedGlobal[position].calories
        time?.text = listRecommendedGlobal[position].time


        itemName?.text = listRecommendedGlobal[position].title
        itemPrice?.text = listRecommendedGlobal[position].price.toString()
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

        btnAddToBag?.setOnClickListener {

            val timeBag = listRecommendedGlobal[position].time
            val name = listRecommendedGlobal[position].title
            val quantity = count
            val imageBag = listRecommendedGlobal[position].image
            val price = listRecommendedGlobal[position].price

            val totalAmount = price?.times(quantity)


            val item = BagItems(timeBag, name, quantity, imageBag, totalAmount)
            var flag = false
            var pos = 0
            if (listBagGlobal.isNotEmpty()) {
                for (i in 0 until listBagGlobal.size) {
                    if (listBagGlobal[i].name == item.name) {
                        flag = true
                        pos =i
                    }
                }
                if (flag) {
                    val quantityNew =
                        item.quantity?.let { it1 ->
                            listBagGlobal[pos].quantity?.plus(it1)
                        }
                    val priceNew = item.price?.let { it2->
                        listBagGlobal[pos].price?.plus(it2)
                    }
                    item.quantity = quantityNew
                    val hashMap = HashMap<String, Any>()
                    val key = listBagGlobal[pos].key
                    if (quantityNew != null && priceNew!=null) {
                        hashMap["quantity"] = quantityNew
                        hashMap["price"] = priceNew
                    }
                    if (key != null) {
                        updateBagItems(key, hashMap)
                    }
                } else {
                    pushBagItems(item)
                }

            }else if(listBagGlobal.isNullOrEmpty()){
                Log.e("TAG", "onRecommendedItemClick: $listBagGlobal" )
                pushBagItems(item)
            }
            bottomSheetDialog.dismiss()
        }


        Glide.with(this).load(listRecommendedGlobal[position].image)
            .placeholder(R.drawable.placeholder2).into(image!!)

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }

    private fun getBagData() {
        val list = ArrayList<BagItems>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(BagItems::class.java)
                        if (item != null && !list.contains(item)) {
                            item.key = dataSnapshot.key
                            list.add(item)
                        }
                    }
                    listBagGlobal = list

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_LONG).show()
            }

        }
        daoBag.get().addValueEventListener(valueEventListener)
    }

    private fun openOrderActivity(type: Int) {
        val intent = Intent(requireContext(), OrderActivity::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_items, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.signOut -> {
                auth.signOut()
                if (auth.currentUser == null) {
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT)
                        .show()
                    Intent(requireContext(), LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }

        return true
    }

    private fun initAllDao() {
        auth = Firebase.auth
        val uId = auth.currentUser?.uid

        daoBag = Dao(
            Firebase.database.getReference(uId.toString()).child(BagItems::class.java.simpleName)
        )

        daoRecommended =
            Dao(Firebase.database.getReference(RecommendedItems::class.java.simpleName))
        daoCategory = Dao(Firebase.database.getReference(CategoryItems::class.java.simpleName))
    }

    private fun pushBagItems(item: BagItems) {
        daoBag.add(item).addOnCompleteListener {
            if(it.isSuccessful)
            showSnackbar()
        }
    }

    private fun updateBagItems(key: String, hashMap: HashMap<String, Any>) {
        daoBag.update(key, hashMap).addOnCompleteListener {
            if (it.isSuccessful) {
                showSnackbar()
            }
        }
    }

    private fun showSnackbar() {
        Snackbar.make(binding.root, "Items in bag", Snackbar.LENGTH_LONG)
            .setAction(
                "Go to bag"
            ) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flMainACtivity, BagFragment()).commit()
                activity?.findViewById<BottomNavigationView>(R.id.bottomNavBar)?.selectedItemId =
                    R.id.bagNavBar
            }.setAnchorView(R.id.bottomNavBar)
            .setBackgroundTint(resources.getColor(R.color.green))
            .setActionTextColor(resources.getColor(R.color.white))
            .show()
    }
}