package the.mrsmile.foodorderer.fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.FragmentCompleteProfileBinding
import the.mrsmile.foodorderer.models.User

class CompleteProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteProfileBinding
    private lateinit var etName: TextInputEditText
    private lateinit var etMobile: TextInputEditText
    private lateinit var etHouse: TextInputEditText
    private lateinit var etArea: TextInputEditText
    private lateinit var etPincode: TextInputEditText
    private lateinit var etCity: TextInputEditText
    private lateinit var btnSubmit: MaterialButton
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var dao: Dao
    private lateinit var auth: FirebaseAuth
    private lateinit var homeAddType: TextView
    private lateinit var workAddType: TextView
    private lateinit var otherAddType: TextView
    private lateinit var cbIsDefault : CheckBox
    private lateinit var navBar: BottomNavigationView
    private lateinit var _activity: AppCompatActivity
    private lateinit var _context : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCompleteProfileBinding.inflate(layoutInflater)
        initStuff()
        onClickHome()

        _activity = activity as AppCompatActivity
        navBar = _activity.findViewById(R.id.bottomNavBar)!!
        _activity.window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            _activity.window?.decorView!!.getWindowVisibleDisplayFrame(r)

            val height = _activity.window?.decorView!!.height
            if (height - r.bottom > height * 0.1399) {
                //keyboard is open
                navBar.visibility = View.GONE
            } else {
                //keyboard is close
                navBar.visibility = View.VISIBLE
            }
        }

        btnSubmit.setOnClickListener {

            val name = etName.text.toString()
            val mobileNo = etMobile.text.toString()
            val houseNo = etHouse.text.toString()
            val area = etArea.text.toString()
            val pincode = etPincode.text.toString()
            val city = etCity.text.toString()
            val email = auth.currentUser?.email.toString()
            val addressType = selectedAddressType()
            var isDefault = false


            if (name.isNotEmpty()
                && mobileNo.isNotEmpty()
                && houseNo.isNotEmpty()
                && area.isNotEmpty()
                && pincode.isNotEmpty()
                && city.isNotEmpty()
                && addressType != null
            ) {

                progressBar.visibility = View.VISIBLE
                if(cbIsDefault.isChecked){
                    isDefault = true
                }
                val user = User(name, mobileNo, email, houseNo, area, pincode, city, addressType,isDefault)
                dao.addUserInfo(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressBar.hide()
                        (_activity).supportFragmentManager.beginTransaction()
                            .remove(this)
                            .replace(R.id.flMainACtivity, BagFragment()).addToBackStack(null)
                            .commit()
                    }

                }
            } else {
                val errorMsg = "Required Field"
                when {
                    name.isEmpty() -> etName.error = errorMsg
                    mobileNo.isEmpty() -> etMobile.error = errorMsg
                    houseNo.isEmpty() -> etHouse.error = errorMsg
                    area.isEmpty() -> etArea.error = errorMsg
                    pincode.isEmpty() -> etPincode.error = errorMsg
                    city.isEmpty() -> etCity.error = errorMsg
                    mobileNo.length > 10 -> etMobile.error = "Invalid mobile number!"
                }
            }
        }

        homeAddType.setOnClickListener {
            onClickHome()
            unselectOther()
            unselectWork()
        }
        workAddType.setOnClickListener {
            onClickWork()
            unselectHome()
            unselectOther()
        }
        otherAddType.setOnClickListener {
            onClickOther()
            unselectHome()
            unselectWork()
        }

        return binding.root
    }


    private fun initStuff() {
        etName = binding.etNameUser
        etMobile = binding.etMobileNoUser
        etHouse = binding.etHouseNoUser
        etArea = binding.etArea
        etPincode = binding.etPincodeUser
        btnSubmit = binding.btnSubmitCPF
        homeAddType = binding.tvAddressTypeHome
        workAddType = binding.tvAddressTypeWork
        otherAddType = binding.tvAddressTypeOther
        progressBar = binding.progressBarCPF
        etCity = binding.etCity
        auth = Firebase.auth
        _context = binding.root.context
        cbIsDefault = binding.cbDefaultCPF
        dao = Dao(
            Firebase.database.getReference(auth.currentUser?.uid.toString())
                .child(User::class.java.simpleName)
        )
    }

    private fun onClickHome() {
        homeAddType.setBackgroundResource(R.drawable.on_click_address)
        homeAddType.setTextColor(ContextCompat.getColor(_context,R.color.white))
    }

    private fun unselectHome() {
        homeAddType.setBackgroundResource(R.drawable.address_type_tv_bg)
        homeAddType.setTextColor(ContextCompat.getColor(_context,android.R.color.tab_indicator_text))
    }

    private fun onClickWork() {
        workAddType.setBackgroundResource(R.drawable.on_click_address)
        workAddType.setTextColor(ContextCompat.getColor(_context,R.color.white))
    }

    private fun unselectWork() {
        workAddType.setBackgroundResource(R.drawable.address_type_tv_bg)
        workAddType.setTextColor(ContextCompat.getColor(_context,android.R.color.tab_indicator_text))
    }

    private fun onClickOther() {
        otherAddType.setBackgroundResource(R.drawable.on_click_address)
        otherAddType.setTextColor(ContextCompat.getColor(_context,R.color.white))
    }

    private fun unselectOther() {
        otherAddType.setBackgroundResource(R.drawable.address_type_tv_bg)
        otherAddType.setTextColor(ContextCompat.getColor(_context,android.R.color.tab_indicator_text))
    }

    private fun selectedAddressType(): String? {
        when {
            homeAddType.currentTextColor == ContextCompat.getColor(_context,R.color.white) -> return "Home"
            workAddType.currentTextColor == ContextCompat.getColor(_context,R.color.white) -> return "Work"
            otherAddType.currentTextColor == ContextCompat.getColor(_context,R.color.white) -> return "Other"
        }
        return null
    }
}