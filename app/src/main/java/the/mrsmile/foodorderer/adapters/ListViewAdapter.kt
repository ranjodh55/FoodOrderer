package the.mrsmile.foodorderer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import the.mrsmile.foodorderer.R
import the.mrsmile.foodorderer.models.User

class ListViewAdapter(
    val contextt: Context, private val list: ArrayList<User>
) :
    ArrayAdapter<User>(contextt, R.layout.listview_items, list) {

    //    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//
//        if (convertView == null) {
////            convertView = LayoutInflater.from(contextt).inflate(R.layout.listview_items,parent,false)
//
//            val tvAddressType = convertView?.findViewById<TextView>(R.id.tvAddressType_LV)
//            val tvAddress = convertView?.findViewById<TextView>(R.id.tvAddress_LV)
//            val address = "${user.houseNo} +${user.area} +${user.city}, ${user.pincode}"
//
//            tvAddressType?.text = user.addressType
//            tvAddress?.text = address
//        }
//        return super.getView(position, convertView, parent)
//    }
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//
//        val user = list[position]
//        Log.e("TAG", "getView: $list", )
//        val view = LayoutInflater.from(context).inflate(R.layout.listview_items, parent, false)
//        val tvAddressType = view.findViewById<TextView>(R.id.tvAddressType_LV)
//        val tvAddress = view.findViewById<TextView>(R.id.tvAddress_LV)
//        val address = "${user.houseNo} +${user.area} +${user.city}, ${user.pincode}"
//
//
//        tvAddressType?.text = user.addressType
//        tvAddress?.text = address
//
//        return view
//    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val user = list[position]
//        Log.e("TAG", "getView: $list")
        val view : View = LayoutInflater.from(contextt).inflate(R.layout.listview_items, parent, false)
        val tvAddressType = view.findViewById<TextView>(R.id.tvAddressType_LV)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress_LV)
        val address = "${user.houseNo} ${user.area} ${user.city}, ${user.pincode}"


        tvAddressType?.text = user.addressType
        tvAddress?.text = address

        return view
    }
}