package com.aprosoft.apfwakotlin.Billing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.custom_billing_list.view.*
import kotlinx.android.synthetic.main.custom_billing_list.view.tv_farmer_name
import kotlinx.android.synthetic.main.custom_list_nursery.view.*
import kotlinx.android.synthetic.main.customer_farmer_list.view.*
import org.json.JSONArray

class BillingListAdapter(val context: Context,
                         val array: JSONArray) : BaseAdapter() {

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
        val rowView = inflater.inflate(R.layout.custom_billing_list, p2, false)

        val billingObject = array.getJSONObject(p0)
        rowView.tv_farmer_name.text = billingObject.getString("farmer_name")
        rowView.tv_billingID.text = billingObject.getString("bill_no")
        rowView.tv_regNo.text = billingObject.getString("farmer_reg_no")
        rowView.tv_farmer_billing_address.text = billingObject.getString("farmer_address")
        rowView.tv_farmer_billing_amount.text = billingObject.getString("bill_net_amount")

//        rowView.tv_nursery_name.text = tempNurseryObject.getString("nursery_name")
//        rowView.tv_nursery_owner.text = tempNurseryObject.getString("nursery_owner")
//        rowView.tv_nursery_address.text = tempNurseryObject.getString("nursery_address")

        return rowView
    }
}