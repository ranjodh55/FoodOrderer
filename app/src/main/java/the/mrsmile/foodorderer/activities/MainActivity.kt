package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter.OnClickInterface
import the.mrsmile.foodorderer.adapters.RecyclerAdapter.onClick
import androidx.viewpager2.widget.ViewPager2
import the.mrsmile.foodorderer.models.RecommendedItems
import the.mrsmile.foodorderer.adapters.ViewPagerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import the.mrsmile.foodorderer.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import the.mrsmile.foodorderer.adapters.RecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.cardview.widget.CardView
import android.content.Intent
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(), OnClickInterface, onClick {
    private var viewPager: ViewPager2? = null
    private val list: MutableList<Int> = ArrayList()
    private lateinit var binding: ActivityMainBinding
    private var toolbar: Toolbar? = null
    private var adapter: ViewPagerAdapter? = null
    private val dao = Dao()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewPager = binding.viewPager
        adapter = ViewPagerAdapter(list, this)
        val profile = binding.ivProfile
        val address = binding.tvAddress
        val addressArrow = binding.ivAddressArrow

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

        addressArrow.setOnClickListener {
            onAddressClick()
        }

        getData()

        //list pf images for viewPager
        list.add(R.drawable.burger)
        list.add(R.drawable.momos2)
        list.add(R.drawable.paneertikka2)
        list.add(R.drawable.pizza)
        list.add(R.drawable.chaampp)



        viewPager!!.adapter = adapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(
            tabLayout, viewPager!!
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = "Burger "
                }
                1 -> {
                    tab.text = "Momos "
                }
                2 -> {
                    tab.text = "Paneer Tikka "
                }
                3 -> {
                    tab.text = "Pizza "
                }
                4 -> {
                    tab.text = "Chaamp "
                }
                else -> {
                    tab.text = "Position$position"
                }
            }
        }.attach()


    }

    private fun getData(): Boolean {

        var flag = false
        dao.get().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val listRecycler = ArrayList<RecommendedItems>()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(RecommendedItems::class.java)

                    Log.e("ITEM_LOG", dataSnapshot.key.toString())
                    if (item != null && !listRecycler.contains(item)) {
                        listRecycler.add(item)
                    }
                }

                val recyclerView = binding.recyclerRecommended
                val adapter1 = RecyclerAdapter(listRecycler, this@MainActivity)
                Log.e("LIST_RECYCLER1", listRecycler.toString())
                recyclerView.adapter = adapter1
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                flag = true
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Error loading items", Snackbar.LENGTH_LONG).show()
            }

        })

        return flag
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
        val currentLocation = bottomSheetDialog.findViewById<CardView>(R.id.cardView_bottomSheet)
        currentLocation?.setOnClickListener {
            Toast.makeText(
                binding.root.context, "Clicked", Toast.LENGTH_SHORT
            ).show()
        }
        bottomSheetDialog.show()
    }

    override fun onRecommendedItemClick(position: Int) {

    }

    private fun openOrderActivity(type: Int) {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
    }
}