package the.mrsmile.foodorderer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RecommendedItems(
    val title: String? = null,
    val desc: String? = null,
    val price: String? = null,
    val image: String? = null
)
