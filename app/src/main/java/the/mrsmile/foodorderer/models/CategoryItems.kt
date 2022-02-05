package the.mrsmile.foodorderer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CategoryItems(
    val image: String? = null,
    val tabTitle: String? = null
)