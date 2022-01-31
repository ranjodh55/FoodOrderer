package the.mrsmile.foodorderer.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import the.mrsmile.foodorderer.models.RecommendedItems

class Dao {

    val databaseReference : DatabaseReference

    init{
        val db = Firebase.database
        databaseReference = db.getReference(RecommendedItems::class.java.simpleName)
    }

    fun get() : Query {
        return databaseReference.orderByKey()
    }
}