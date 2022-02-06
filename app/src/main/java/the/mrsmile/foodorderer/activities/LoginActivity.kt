package the.mrsmile.foodorderer.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.database.Auth
import the.mrsmile.foodorderer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email : EditText
    private lateinit var pass : EditText
    private val log = "LoginActivity"
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        email = binding.etEmailSignUp
        pass = binding.etPassSignUp

        binding.btnSignUp.setOnClickListener {
            signUp(email.text.toString(),pass.text.toString())
        }
    }

    private fun signUp(email : String,pass:String){
        hideKeyboard(this)
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show()
                Intent(this,MainActivity::class.java).apply{
                    startActivity(this)
                }
            }
            else{
                Snackbar.make(binding.root,"Something went wrong",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun hideKeyboard(activity : Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity);
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0);

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}