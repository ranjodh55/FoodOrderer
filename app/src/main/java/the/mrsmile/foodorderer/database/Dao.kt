package the.mrsmile.foodorderer.database

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import the.mrsmile.foodorderer.models.BagItems

class Dao(private var databaseReference : DatabaseReference) {

    fun get() : Query {
        return databaseReference.orderByKey()
    }

    fun add(item : BagItems) : Task<Void> {
        return databaseReference.push().setValue(item)
    }

    fun update(key:String,hashMap: HashMap<String,Any>) : Task<Void>{
        return databaseReference.child(key).updateChildren(hashMap)
    }
    fun remove(key: String) : Task<Void> {
        return databaseReference.child(key).removeValue()
    }
}