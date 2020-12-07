package com.aprosoft.apfwakotlin.Billing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_billing_list.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_list)

        nurseryBillList()
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
                    val billListArray = jsonObject.getJSONArray("nursery_owner_bill_list")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    val billingListAdapter = BillingListAdapter(this@BillingListActivity,billListArray)
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