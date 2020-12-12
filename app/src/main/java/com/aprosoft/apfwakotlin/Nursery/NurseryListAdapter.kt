package com.aprosoft.apfwakotlin.Nursery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.custom_list_nursery.view.*
import org.json.JSONArray

class NurseryListAdapter(val context: Context,
                         var array:JSONArray) : BaseAdapter() {

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
        val rowView = inflater.inflate(R.layout.custom_list_nursery, p2, false)

        val tempNurseryObject = array.getJSONObject(p0)
        rowView.tv_nursery_name.text = tempNurseryObject.getString("nursery_name")
        rowView.tv_nursery_owner.text = tempNurseryObject.getString("nursery_owner")
        rowView.tv_state.text = tempNurseryObject.getString("state_name")
        rowView.tv_district.text = tempNurseryObject.getString("district_name")
        rowView.tv_nursery_address.text = tempNurseryObject.getString("nursery_address")

        return rowView
    }

    fun reloadList(array: JSONArray) {
        this.array = array
        this.notifyDataSetChanged()
    }
}