package the.mrsmile.foodorderer.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class Dao(private var databaseReference : DatabaseReference) {



    fun get() : Query {
        return databaseReference.orderByKey()
    }
}