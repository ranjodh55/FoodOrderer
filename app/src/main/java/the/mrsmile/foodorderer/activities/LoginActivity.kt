package the.mrsmile.foodorderer.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.database.Dao
import the.mrsmile.foodorderer.databinding.ActivityLoginBinding
import the.mrsmile.foodorderer.databinding.CollectUserInfoBinding
import the.mrsmile.foodorderer.models.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var progressBar: LinearProgressIndicator
    private val log = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private lateinit var nameUser: TextInputEditText
    private lateinit var mobileUser: TextInputEditText
    private lateinit var houseNoUser: TextInputEditText
    private lateinit var areaUser: TextInputEditText
    private lateinit var pincodeUser: TextInputEditText
    private lateinit var dao: Dao
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        auth = Firebase.auth
        email = binding.etEmailSignUp
        pass = binding.etPassSignUp
        progressBar = binding.progressBarLoginActivity

        binding.btnSignUp.setOnClickListener {

//            showDialog()
            hideKeyboard(this)
            if (email.text.isNotEmpty() && pass.text.isNotEmpty())
                signUp(email.text.toString(), pass.text.toString())
            else Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp(email: String, pass: String) {

        if (!email.startsWith("mrsmileisgod")) {
            progressBar.show()
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    progressBar.hide()
//                startMainActivity()
                    showDialog()
//                finish()
                } else {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT)
                                .show()
                            progressBar.hide()
                            startMainActivity()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Something's not right I can feel it.",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.hide()
                        }
                    }
                }
            }
        }else{
            this.email.error = "User already exists!"
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startMainActivity()
            finish()
        }

    }

    private fun showDialog() {
        val dialogBinding = CollectUserInfoBinding.inflate(layoutInflater)
        customAlertDialogView = dialogBinding.root
        nameUser = dialogBinding.etNameUser
        mobileUser = dialogBinding.etMobileNoUser
        houseNoUser = dialogBinding.etHouseNoUser
        areaUser = dialogBinding.etArea
        pincodeUser = dialogBinding.etPincodeUser

        nameUser.requestFocus()

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Complete your profile")
            .setMessage("Finish to setup address automatically")
            .setPositiveButton("Add", null)
            .setNegativeButton("Do it later") { dialog, _ ->
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    dialog.dismiss()
                    startMainActivity()
                }
            }

        val dialog = materialAlertDialogBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            if (nameUser.text?.isNotEmpty() == true
                && mobileUser.text?.isNotEmpty() == true
                && houseNoUser.text?.isNotEmpty() == true
                && areaUser.text?.isNotEmpty() == true
                && pincodeUser.text?.isNotEmpty() == true
            ) {
                val name = nameUser.text.toString()
                val mobileNo = mobileUser.text.toString()
                val houseNo = houseNoUser.text.toString()
                val area = areaUser.text.toString()
                val pincode = pincodeUser.text.toString()
                val email = email.text.toString()

                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid
                    dao =
                        Dao(
                            Firebase.database.getReference(uid)
                                .child(User::class.java.simpleName)
                        )
                    val user = User(name, mobileNo, email, houseNo, area, pincode)
                    Log.e("TAG", "showDialog:")
                    dao.addUserInfo(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            startMainActivity()
                            finish()
                        } else {
                            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Log.e("TAG", "showDialog: else")
                if (nameUser.text.isNullOrEmpty())
                    nameUser.error = "Mandatory Field"

                if (mobileUser.text.isNullOrEmpty())
                    mobileUser.error = "Mandatory Field"

                if (houseNoUser.text.isNullOrEmpty())
                    houseNoUser.error = "Mandatory Field"

                if (areaUser.text.isNullOrEmpty())
                    areaUser.error = "Mandatory Field"

                if (pincodeUser.text.isNullOrEmpty())
                    pincodeUser.error = "Mandatory Field"
            }
        }
    }
}