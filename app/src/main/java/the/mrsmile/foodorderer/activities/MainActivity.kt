package the.mrsmile.foodorderer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentResultListener
import the.mrsmile.foodorderer.fragments.BagFragment
import the.mrsmile.foodorderer.fragments.HomeFragment
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.databinding.ActivityMainBinding
import the.mrsmile.foodorderer.fragments.ProfileFragment


class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomAppBar: BottomNavigationView
    private var dumbCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()
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
            dumbCounter++
        }
    }

    fun initViews() {
        bottomAppBar = binding.bottomNavBar
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
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, error: String?) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
    }
}