package com.aprosoft.apfwakotlin.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.custom_list_team.view.*
import org.json.JSONArray

class TeamListAdapte(val context: Context,
                     val array: JSONArray
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
        val rowView = inflater.inflate(R.layout.custom_list_team, p2, false)

        val tempNurseryObject = array.getJSONObject(p0)
        rowView.tv_team_name.text = tempNurseryObject.getString("role_name")
        rowView.tv_team_count.text = tempNurseryObject.getString("count")

        return rowView
    }
}