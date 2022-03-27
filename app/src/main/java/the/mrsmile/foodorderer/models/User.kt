package the.mrsmile.foodorderer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val name: String? = null,
    val mobileNo: String? = null,
    val email: String? = null,
    val houseNo: String? = null,
    val area: String? = null,
    val pincode: String? = null,
    val city: String? = null,
    val addressType: String? = null,
    val default : Boolean = false
)