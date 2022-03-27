package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import the.mrsmile.foodorderer.fragments.BagFragment
import the.mrsmile.foodorderer.fragments.HomeFragment
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityMainBinding
import the.mrsmile.foodorderer.fragments.ProfileFragment
import the.mrsmile.foodorderer.models.BagItems


class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomAppBar: BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var daoBag: Dao
    private var dumbCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()

        Checkout.preload(applicationContext)
        bottomAppBar.selectedItemId = R.id.homeNavBar
        currentFragment(HomeFragment())
        bottomAppBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.homeNavBar -> supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.flMainACtivity,
                        HomeFragment(), "Home"
                    )
                    .addToBackStack(null)
                    .commit()

                R.id.bagNavBar -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flMainACtivity, BagFragment())
                    .addToBackStack("Home")
                    .commit()

                R.id.AccountNavBar -> supportFragmentManager
                    .beginTransaction().replace(R.id.flMainACtivity, ProfileFragment())
                    .addToBackStack("Home")
                    .commit()
            }
            true
        }

        bottomAppBar.setOnItemReselectedListener {
        }
    }

    private fun initViews() {
        bottomAppBar = binding.bottomNavBar
        auth = Firebase.auth
        daoBag = Dao(
            Firebase.database.getReference(auth.currentUser?.uid.toString())
                .child(BagItems::class.java.simpleName)
        )
    }

    private fun currentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flMainACtivity, fragment).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bottomAppBar.selectedItemId = R.id.homeNavBar
    }

    override fun onPaymentSuccess(p0: String?) {

        daoBag.removeBagItems().addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(p0: Int, error: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
    }
}