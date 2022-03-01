package the.mrsmile.foodorderer.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import the.mrsmile.foodorderer.activities.LoginActivity
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.FragmentProfileBinding
import the.mrsmile.foodorderer.models.User

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dao: Dao
    private var currentUser: FirebaseUser? = null
    private lateinit var name: TextView
    private lateinit var email: TextView
    private val TAG = "ProfileFragment"
    private lateinit var btnLogout: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        initStuff()
        getUserData()

        btnLogout.setOnClickListener {
            if (auth.currentUser != null)
                auth.signOut()
            Intent(requireContext(),LoginActivity::class.java).apply {
                startActivity(this)
                activity?.finish()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
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
    }

    private fun getUserData() {
        var user: User?
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User::class.java)
                    name.text = user?.name
                    email.text = user?.email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
            }

        }
        dao.get().addListenerForSingleValueEvent(valueEventListener)
    }
}