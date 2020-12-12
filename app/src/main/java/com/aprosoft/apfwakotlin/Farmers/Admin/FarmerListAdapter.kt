package com.aprosoft.apfwakotlin.Farmers.Admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.customer_farmer_list.view.*
import org.json.JSONArray

class FarmerListAdapter(val context: Context,
                        var array: JSONArray
) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return array.length()
    }

    override fun getItem(p0: Int): Any {
        return array.getJSONObject(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.customer_farmer_list, p2, false)

        val tempNurseryObject = array.getJSONObject(p0)
        rowView.tv_farmer_name.text = tempNurseryObject.getString("farmer_name")
        rowView.tv_farmer_address.text = tempNurseryObject.getString("farmer_address")
        rowView.tv_farmer_reg_no.text = tempNurseryObject.getString("farmer_reg_no")
        rowView.tv_farmer_number.text = tempNurseryObject.getString("farmer_mobile")
        rowView.tv_farmer_adhaar.text = tempNurseryObject.getString("farmer_adhar_no")

        return rowView
    }

    fun reloadList(array: JSONArray) {
        this.array = array
        this.notifyDataSetChanged()
    }
}