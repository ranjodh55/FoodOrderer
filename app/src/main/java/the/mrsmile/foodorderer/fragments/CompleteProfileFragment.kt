package the.mrsmile.foodorderer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var btnSubmit: MaterialButton
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var dao: Dao
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCompleteProfileBinding.inflate(layoutInflater)
        initStuff()

        btnSubmit.setOnClickListener {

            val name = etName.text.toString()
            val mobileNo = etMobile.text.toString()
            val houseNo = etHouse.text.toString()
            val area = etArea.text.toString()
            val pincode = etPincode.text.toString()
            val email = auth.currentUser?.email.toString()


            if (name.isNotEmpty()
                && mobileNo.isNotEmpty()
                && houseNo.isNotEmpty()
                && area.isNotEmpty()
                && pincode.isNotEmpty()
            ) {

                progressBar.visibility = View.VISIBLE
                val user = User(name, mobileNo, email, houseNo, area, pincode)
                dao.addUserInfo(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressBar.hide()
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .remove(this)
                            .replace(R.id.flMainACtivity, BagFragment()).addToBackStack(null).commit()
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
                }
            }
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
        progressBar = binding.progressBarCPF
        auth = Firebase.auth
        dao = Dao(
            Firebase.database.getReference(auth.currentUser?.uid.toString())
                .child(User::class.java.simpleName)
        )

    }
}