package the.mrsmile.foodorderer.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.activities.LoginActivity
import the.mrsmile.foodorderer.adapters.ListViewAdapter
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.FragmentProfileBinding
import the.mrsmile.foodorderer.databinding.SavedAddressBottomSheetBinding
import the.mrsmile.foodorderer.models.User

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dao: Dao
    private var currentUser: FirebaseUser? = null
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var llSavedAddress: LinearLayout
    private lateinit var llMyOrders: LinearLayout
    private val TAG = "ProfileFragment"
    private lateinit var btnLogout: MaterialButton
    private var listUser = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        initStuff()
        hideStuff()
        getUserData()

        btnLogout.setOnClickListener {
            auth.signOut()
            Intent(requireContext(), LoginActivity::class.java).apply {
                startActivity(this)
                (activity as AppCompatActivity).finish()
            }
        }


        llSavedAddress.setOnClickListener {
            var address = ""
            val bindingBS = SavedAddressBottomSheetBinding.inflate(layoutInflater)
            val bottomSheet = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(bindingBS.root)
            val listView = bindingBS.lvAddressBottomSheet
            val adapter = ListViewAdapter(requireContext(), listUser)
            bindingBS.lvAddressBottomSheet.adapter = adapter
            bindingBS.lvAddressBottomSheet.isClickable = true

            if (listUser.size == 3) {
                bindingBS.btnAddAddress.hide()
            }

            bindingBS.cbMakeDefault.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    bindingBS.btnContinueSavedAddresses.visibility = View.VISIBLE
                }else{
                    bindingBS.btnContinueSavedAddresses.visibility = View.GONE
                }
            }


            listView.setOnItemClickListener { adapterView, view, position, l ->
                val user = listUser[position]
                address = user.houseNo + " " + user.area + ", " + user.pincode

                view.setBackgroundResource(R.drawable.on_click_saved_address)
                bindingBS.cbMakeDefault.visibility = View.VISIBLE
                if(bindingBS.cbMakeDefault.isChecked){
                    bindingBS.btnContinueSavedAddresses.visibility = View.VISIBLE
                }

                bindingBS.btnRemoveAddress.visibility = View.VISIBLE
                when (position) {
                    0 -> {
                        listView[1].setBackgroundResource(android.R.color.transparent)
                        listView[2].setBackgroundResource(android.R.color.transparent)
                    }
                    1 -> {
                        listView[0].setBackgroundResource(android.R.color.transparent)
                        listView[2].setBackgroundResource(android.R.color.transparent)
                    }
                    2 -> {
                        listView[0].setBackgroundResource(android.R.color.transparent)
                        listView[1].setBackgroundResource(android.R.color.transparent)
                    }
                }
            }

            bindingBS.btnAddAddress.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.flMainACtivity, CompleteProfileFragment()).commit()
                bottomSheet.dismiss()
            }
            bottomSheet.show()
        }
        return binding.root
    }

    private fun hideStuff() {
        binding.clProfile.visibility = View.GONE
    }

    private fun initStuff() {
        auth = Firebase.auth
        currentUser = auth.currentUser
        dao = Dao(
            Firebase.database.getReference(currentUser?.uid.toString())
                .child(User::class.java.simpleName)
        )
        Log.e(TAG, "initStuff: ${currentUser?.uid} ")
        email = binding.tvProfileEmail
        name = binding.tvProfileName
        btnLogout = binding.btnLogoutProfile
        llMyOrders = binding.llMyOrders
        llSavedAddress = binding.llMyAddresses
    }

    private fun getUserData() {
        val list = ArrayList<User>()
        var user: User?
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        user = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            list.add(user!!)
                        }
                    }
                    listUser = list
                    binding.clProfile.visibility = View.VISIBLE
                    binding.progressBarProfile.hide()
                    name.text = list[0].name
                    email.text = list[0].email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }

        }
        dao.get().addListenerForSingleValueEvent(valueEventListener)
    }
}