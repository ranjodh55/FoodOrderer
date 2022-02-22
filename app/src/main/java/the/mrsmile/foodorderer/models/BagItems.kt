package the.mrsmile.foodorderer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BagItems(
    val time: String? = null,
    val name: String? = null,
    var quantity: Int? = null,
    val image: String? = null,
    var price: Int? = null,
    var key: String? = null
)