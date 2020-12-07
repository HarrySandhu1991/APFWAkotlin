package com.aprosoft.apfwakotlin.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.custom_list_dashboard.view.*
import org.json.JSONArray

class PromotionalAdapter(val context: Context,
                         val array: JSONArray,
                         val imgBaseUrl: String
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
        val rowView = inflater.inflate(R.layout.custom_list_dashboard, p2, false)

        val tempNurseryObject = array.getJSONObject(p0)
        rowView.tv_promotional_title.text = tempNurseryObject.getString("material_title")

        if (tempNurseryObject.getString("material_type").equals("3")) {
            rowView.iv_thumbnail.setBackgroundColor(Color.BLACK)
            rowView.iv_thumbnail.alpha = 0.5F
        } else {
            rowView.iv_thumbnail.setBackgroundColor(Color.WHITE)
            rowView.iv_thumbnail.alpha = 1.0F
            val image = tempNurseryObject.getString("material_file")
            Glide.with(context).load("${imgBaseUrl}${image}").into(rowView.iv_thumbnail)
        }


        return rowView
    }
}