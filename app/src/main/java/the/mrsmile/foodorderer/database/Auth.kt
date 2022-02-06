package the.mrsmile.foodorderer.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {

    private val auth: FirebaseAuth = Firebase.auth
    var flag = false

    fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                flag = true
            }
        }
    }
}