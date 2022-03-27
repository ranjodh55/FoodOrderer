package the.mrsmile.foodorderer.activities

import android.app.Activity
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
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var progressBar: LinearProgressIndicator
    private val log = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
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
            hideKeyboard(this)
            if (email.text.isNotEmpty() && pass.text.isNotEmpty())
                signUp(email.text.toString(), pass.text.toString())
            else Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }
        email.addTextChangedListener {
            if (it?.contains(".com") == true) {
                pass.requestFocus()
            }
        }
    }

    private fun signUp(email: String, pass: String) {

        if (!email.startsWith("mrsmileisgod")) {
            progressBar.show()
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    progressBar.hide()
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Check your inbox and verify email.",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.hide()
                        }
                    }
                } else {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (auth.currentUser?.isEmailVerified == true) {
                                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT)
                                    .show()
                                progressBar.hide()
                                startMainActivity()
                                finish()
                            } else {
                                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Check your inbox and verify email.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        progressBar.hide()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Something's not right I can feel it. ",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        progressBar.hide()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Something's not right I can feel it. ",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.hide()
                        }
                    }
                }
            }
        } else {
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
        Log.e("TAG", "hideKeyboard: ${imm.isActive}")

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            startMainActivity()
            finish()
        }
    }
}