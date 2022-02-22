package the.mrsmile.foodorderer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RecommendedItems(
    val title: String? = null,
    val desc: String? = null,
    val price: Int? = null,
    val image: String? = null,
    val rating: String? = null,
    val calories: String? = null,
    val time: String? = null
)
