package com.aprosoft.apfwakotlin.Billing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_billing_list.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_farmer_list.*
import kotlinx.android.synthetic.main.activity_nursery_list.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillingListActivity : AppCompatActivity() {

    var billListArray:JSONArray?= null
    var billListArrayCopy:JSONArray?= null
    var billingListAdapter:BillingListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_list)




        var role_id = Singleton().getUserRoleFromSavedUser(this)

        if (role_id == "5" || role_id == "farmer") {
            btn_add_bill.visibility = View.VISIBLE
        }
        if (role_id == "farmer") {
            farmerBillList()
        } else if (role_id == "1") {
            adminBillList()
        } else {
            nurseryBillList()
        }


        et_search_bill.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                Log.d("T2",et_search_bill.text.toString())
                filterList()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                Log.d("T1",et_search_bill.text.toString())

            }
        })


        et_search_bill.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                Log.d("T2",et_search_bill.text.toString())
                filterList()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                Log.d("T1",et_search_bill.text.toString())

            }
        })

        btn_add_bill.setOnClickListener {
            val intent = Intent(this, AddBillActivity::class.java)
            startActivity(intent)
        }
    }

//
//    fun filterList() {
//        if (farmerListArrayCopy != null) {
//
//            if (adapter != null && et_search_farmer.text.toString().isBlank()) {
//                farmerListArray = farmerListArrayCopy
//                adapter!!.reloadList(farmerListArray!!)
//                return
//            }
//
//
//            farmerListArray = JSONArray()
//            for (i in 0 until farmerListArrayCopy!!.length()) {
//                val tempNursery = farmerListArrayCopy!!.getJSONObject(i)
//                val name = tempNursery.getString("farmer_reg_no")
//                val address = tempNursery.getString("farmer_name")
//                val owner = tempNursery.getString("farmer_address")
//                val state = tempNursery.getString("farmer_mobile")
//                val district = tempNursery.getString("farmer_adhar_no")
//                val whatsapp = tempNursery.getString("farmer_whatsapp_no")
//
//                val searchedText = et_search_farmer.text.toString()
//                when {
//                    name.contains(searchedText, true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                    address.contains(searchedText,true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                    owner.contains(searchedText,true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                    state.contains(searchedText,true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                    district.contains(searchedText,true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                    whatsapp.contains(searchedText,true) -> {
//                        farmerListArray!!.put(tempNursery)
//                    }
//                }
//
//
//            }
//            adapter!!.reloadList(farmerListArray!!)
//        }
//    }




    fun filterList() {
        if (billListArrayCopy != null) {

            if (billingListAdapter != null && et_search_bill.text.toString().isBlank()) {
                billListArray = billListArrayCopy
                billingListAdapter!!.notifyChanges(billListArray!!)
                return
            }


            billListArray = JSONArray()
            for (i in 0 until billListArrayCopy!!.length()) {
                val tempNursery = billListArrayCopy!!.getJSONObject(i)
                val name = tempNursery.getString("bill_date")
                val address = tempNursery.getString("farmer_reg_no")
                val owner = tempNursery.getString("farmer_name")
                val state = tempNursery.getString("farmer_address")
                val district = tempNursery.getString("bill_net_amount")
                val bill_particular = tempNursery.getString("bill_particular")

                val searchedText = et_search_bill.text.toString()
                when {
                    name.contains(searchedText, true) -> {
                        billListArray!!.put(tempNursery)
                    }
                    address.contains(searchedText,true) -> {
                        billListArray!!.put(tempNursery)
                    }
                    owner.contains(searchedText,true) -> {
                        billListArray!!.put(tempNursery)
                    }
                    state.contains(searchedText,true) -> {
                        billListArray!!.put(tempNursery)
                    }
                    district.contains(searchedText,true) -> {
                        billListArray!!.put(tempNursery)
                    }
                    bill_particular.contains(searchedText,true) -> {
                        billListArray!!.put(tempNursery)
                    }
                }


            }
            billingListAdapter!!.notifyChanges(billListArray!!)
        }
    }





    private fun nurseryBillList(){

        val userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        val userId = userObject.getString("user_id")
        val roleId = userObject.getString("role_id")


        val billListParams = HashMap<String,String>()
        billListParams["user_id"] = userId
        billListParams["user_role_id"] = roleId

        val call:Call<ResponseBody> = ApiClient.getClient.nurseryOwnerBillList(billListParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                val msg: String? = null
                if (status == 1) {
                    billListArray = jsonObject.getJSONArray("nursery_owner_bill_list")
                    billListArrayCopy = billListArray
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    billingListAdapter = BillingListAdapter(this@BillingListActivity,billListArray!!)
                    lv_billing_list.adapter= billingListAdapter
                }else {
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun farmerBillList() {
        val userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        val userId = userObject.getString("farmer_id")
        val roleId = userObject.getString("role_id")


        val billListParams = HashMap<String,String>()
        billListParams["farmer_id"] = userId
//        billListParams["user_role_id"] = roleId

        val call:Call<ResponseBody> = ApiClient.getClient.farmerBillList(billListParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                val msg: String? = null
                if (status == 1) {
                    billListArray = jsonObject.getJSONArray("farmer_bill_list")
                    billListArrayCopy = billListArray
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    billingListAdapter = BillingListAdapter(this@BillingListActivity,billListArray!!)
                    lv_billing_list.adapter= billingListAdapter
                }else {
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun adminBillList() {
        val userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        val userId = userObject.getString("user_id")
        val roleId = userObject.getString("role_id")


        val billListParams = HashMap<String,String>()
        billListParams["user_id"] = userId
        billListParams["user_role_id"] = roleId

        val call:Call<ResponseBody> = ApiClient.getClient.adminBillList(billListParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                val msg: String? = null
                if (status == 1) {
                    billListArray = jsonObject.getJSONArray("bill_list")
                    billListArrayCopy = billListArray
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    billingListAdapter = BillingListAdapter(this@BillingListActivity,billListArray!!)
                    lv_billing_list.adapter= billingListAdapter
                }else {
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}