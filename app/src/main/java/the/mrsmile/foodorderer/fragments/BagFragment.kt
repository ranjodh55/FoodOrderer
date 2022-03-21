package the.mrsmile.foodorderer.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import org.json.JSONObject
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.adapters.BagRecyclerAdapter
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.FragmentBagBinding
import the.mrsmile.foodorderer.models.BagItems
import the.mrsmile.foodorderer.models.User

class BagFragment : Fragment(), BagRecyclerAdapter.Click {

    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var binding: FragmentBagBinding
    private lateinit var daoBag: Dao
    private lateinit var daoUser: Dao
    private lateinit var listGlobal: ArrayList<BagItems>
    private lateinit var deliveryIn: TextView
    private lateinit var deliveryAt: TextView
    private lateinit var tvPrice: TextView
    private lateinit var payNow: MaterialButton
    private var totalPrice = 0
    private lateinit var user: User
    private lateinit var contextt: Context
    private lateinit var clMain: ConstraintLayout
    private lateinit var clForEmptyBag: ConstraintLayout
    private lateinit var btnExplore: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBagBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

//        Checkout.preload(requireContext())
        val uid = Firebase.auth.currentUser?.uid
        daoBag = Dao(
            Firebase.database.getReference(uid.toString()).child(BagItems::class.java.simpleName)
        )
        daoUser =
            Dao(Firebase.database.getReference(uid.toString()).child(User::class.java.simpleName))
        contextt = binding.root.context
        initViews()
        hideStuff()
//        getData()
        getUserData()

        payNow.setOnClickListener {
            startPayment(totalPrice)

        }
        btnExplore.setOnClickListener {
            (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavBar).selectedItemId =
                R.id.homeNavBar
        }
        return binding.root
    }

    private fun getData() {
        val list = ArrayList<BagItems>()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(BagItems::class.java)
                        item?.key = dataSnapshot.key
                        if (item != null && !list.contains(item)) {
                            list.add(item)
                        }
                    }
                    setRecyclerView(list)
                    listGlobal = list
                    setTotalPrice(list)
                    showStuff()
                } else {
                    emptyBag()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }
        }
        daoBag.get().addValueEventListener(valueEventListener)
    }

    private fun getUserData() {
        daoUser.get().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val item = snapshot.getValue(User::class.java)
                    if (item != null) {
                        if (item.name?.isNotEmpty() == true
                            && item.email?.isNotEmpty() == true
                            && item.area?.isNotEmpty() == true
                            && item.mobileNo?.isNotEmpty() == true
                            && item.houseNo?.isNotEmpty() == true
                            && item.pincode?.isNotEmpty() == true
                        ) {
                            user = item
                            getData()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please refill your information",
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.flMainACtivity, CompleteProfileFragment())
                        }
                    }

                } else {
//                    showStuff()
                    binding.progressBarBag.hide()
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.flBag, CompleteProfileFragment()).commit()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "error $error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun startPayment(price: Int) {

        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setKeyID("rzp_test_COusGe94oF8tzB")
        co.setImage(R.drawable.ic_launcher_dark)

        try {
            val options = JSONObject()
            options.put("name", "Food Orderer")
            options.put("description", "Order Payment")
            options.put("currency", "INR")
            options.put("amount", price * 100)//pass amount in currency subunits
            options.put("theme.color", "#f54748")

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", user.email)
            prefill.put("contact", user.mobileNo)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: ", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setRecyclerView(list: ArrayList<BagItems>) {
        val recyclerView = binding.rvBag
        val adapter = BagRecyclerAdapter(list, contextt, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(contextt)
        recyclerView.hasFixedSize()
    }

    private fun setTotalPrice(list: ArrayList<BagItems>) {

        var price = 0
        for (i in 0 until list.size) {
            price += list[i].price!!
        }

        val total = "₹$price"
        val text = HtmlCompat.fromHtml(
            "Total : <font color=000><b>$total</b></font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        tvPrice.text = text
        totalPrice = price
    }

    private fun hideStuff() {
        val gone = View.GONE
        clMain.visibility = gone
    }

    private fun showStuff() {
        progressBar.hide()
        val address = user.houseNo + " " + user.area + ", " + user.pincode
        deliveryAt.text = address
        var time = 0
        if (listGlobal.size < 5 && listGlobal.isNotEmpty()) {
            if (listGlobal[0].time != null)
                time = listGlobal[0].time!!
        } else {
            var tempTime = 0
            for (i in 0 until listGlobal.size) {
                if (listGlobal[i].time != null)
                    tempTime += listGlobal[i].time!!
            }
            time = tempTime / 3
        }
        val text = "Delivery in $time minutes"
        deliveryIn.text = text
        clMain.visibility = View.VISIBLE
    }

    private fun initViews() {
        tvPrice = binding.tvTotalBag
        deliveryIn = binding.tvDeliveryInBag
        deliveryAt = binding.tvDeliveryAt
        payNow = binding.btnPayNowBag
        clMain = binding.clBag
        clForEmptyBag = binding.clForEmptyBag
        btnExplore = binding.btnBuyEmptyBag
        progressBar = binding.progressBarBag

    }

    private fun emptyBag() {
        val gone = View.GONE
        val visible = View.VISIBLE
        clForEmptyBag.visibility = visible
        clMain.visibility = gone

    }

    override fun onDecButtonClick(view: View, position: Int) {

        var quantity = listGlobal[position].quantity
        var price = listGlobal[position].price
        val hashMap = HashMap<String, Any>()
        val key = listGlobal[position].key!!
        if (price != null && quantity != null) {
            val singleItemPrice = price / quantity

            if (quantity > 1) {
                quantity--
                price -= singleItemPrice

                hashMap["quantity"] = quantity
                hashMap["price"] = price
                listGlobal[position].quantity = quantity
                listGlobal[position].price = price
                setRecyclerView(listGlobal)
                updateBag(key, hashMap)

            } else if (quantity == 1) {
                listGlobal.removeAt(position)
                setRecyclerView(listGlobal)
                daoBag.remove(key)
            }
        }
    }

    override fun onIncButtonClick(view: View, position: Int) {

        if (listGlobal[position].quantity!! < 10) {

            var quantity = listGlobal[position].quantity
            var price = listGlobal[position].price
            val hashMap = HashMap<String, Any>()
            val key = listGlobal[position].key!!
            if (price != null && quantity != null) {
                val singleItemPrice = price / quantity

                quantity++
                price += singleItemPrice

                hashMap["quantity"] = quantity
                hashMap["price"] = price
                listGlobal[position].quantity = quantity
                listGlobal[position].price = price
                setRecyclerView(listGlobal)
                updateBag(key, hashMap)
            }
        } else {
            Toast.makeText(requireContext(), "Maximum items to buy are 10.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateBag(key: String, hashMap: HashMap<String, Any>) {
        daoBag.update(key, hashMap)
    }
}