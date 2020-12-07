package com.aprosoft.apfwakotlin.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.custom_team_members_list_item.view.*
import org.json.JSONArray

class TeamMemberListAdapter(val context: Context,
                            val array: JSONArray
) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return array.length() + 1
    }

    override fun getItem(p0: Int): Any {
        if (p0 == 0)
            return p0
        return array.getJSONObject(p0-1)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.custom_team_members_list_item, p2, false)


        if (p0 != 0) {


            val tempNurseryObject = array.getJSONObject(p0-1)
            val tempTeamRoles = tempNurseryObject.getJSONObject("role")
            rowView.tv_name.text = tempNurseryObject.getString("user_name")
            rowView.tv_mobile.text = tempNurseryObject.getString("user_mobile")

            rowView.tv_ch.text = tempTeamRoles.getString("Channel Head ")
            rowView.tv_rm.text = tempTeamRoles.getString("Regional Manager")
            rowView.tv_cm.text = tempTeamRoles.getString("Channel Manager")
            rowView.tv_am.text = tempTeamRoles.getString("Area Manager")
            rowView.tv_no.text = tempTeamRoles.getString("Nursery Owner")
            rowView.tv_dh.text = tempTeamRoles.getString("District Head")
            rowView.tv_d.text = tempTeamRoles.getString("Distributors")
            rowView.tv_td.text = tempTeamRoles.getString("Taluka Dealers")

            val ch = tempTeamRoles.getString("Channel Head ")
            val rm = tempTeamRoles.getString("Regional Manager")
            val cm = tempTeamRoles.getString("Channel Manager")
            val am = tempTeamRoles.getString("Area Manager")
            val no = tempTeamRoles.getString("Nursery Owner")
            val dh= tempTeamRoles.getString("District Head")
            val d = tempTeamRoles.getString("Distributors")
            val td = tempTeamRoles.getString("Taluka Dealers")

            if (ch.equals("",true))
                rowView.tv_ch.visibility = View.GONE
            if (rm.equals("",true))
                rowView.tv_rm.visibility = View.GONE
            if (cm.equals("",true))
                rowView.tv_cm.visibility = View.GONE
            if (am.equals("",true))
                rowView.tv_am.visibility = View.GONE
            if (no.equals("",true))
                rowView.tv_no.visibility = View.GONE
            if (dh.equals("",true))
                rowView.tv_dh.visibility = View.GONE
            if (d.equals("",true))
                rowView.tv_d.visibility = View.GONE
            if (td.equals("",true))
                rowView.tv_td.visibility = View.GONE


        } else {
            val tempNurseryObject = array.getJSONObject(p0)
            val tempTeamRoles = tempNurseryObject.getJSONObject("role")

            val ch = tempTeamRoles.getString("Channel Head ")
            val rm = tempTeamRoles.getString("Regional Manager")
            val cm = tempTeamRoles.getString("Channel Manager")
            val am = tempTeamRoles.getString("Area Manager")
            val no = tempTeamRoles.getString("Nursery Owner")
            val dh= tempTeamRoles.getString("District Head")
            val d = tempTeamRoles.getString("Distributors")
            val td = tempTeamRoles.getString("Taluka Dealers")

            if (ch.equals("",true))
                rowView.tv_ch.visibility = View.GONE
            if (rm.equals("",true))
                rowView.tv_rm.visibility = View.GONE
            if (cm.equals("",true))
                rowView.tv_cm.visibility = View.GONE
            if (am.equals("",true))
                rowView.tv_am.visibility = View.GONE
            if (no.equals("",true))
                rowView.tv_no.visibility = View.GONE
            if (dh.equals("",true))
                rowView.tv_dh.visibility = View.GONE
            if (d.equals("",true))
                rowView.tv_d.visibility = View.GONE
            if (td.equals("",true))
                rowView.tv_td.visibility = View.GONE


        }




        return rowView
    }
}