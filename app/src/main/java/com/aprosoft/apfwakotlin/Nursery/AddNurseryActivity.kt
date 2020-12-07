package com.aprosoft.apfwakotlin.Nursery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_add_farmer.*
import kotlinx.android.synthetic.main.activity_add_nursery.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNurseryActivity : AppCompatActivity() {
    var stateList: JSONArray? = null
    var districtList: JSONArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nursery)

        btn_addNurseryDetails.setOnClickListener {

            addNursery()
        }
        getStateList()

    }


    fun getStateList() {
        val call:Call<ResponseBody> = ApiClient.getClient.getStateList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    stateList = jsonObject.getJSONArray("state_list")
                    showStates()
                }else{
                    msg = jsonObject.getString("message")
                }
                Log.d("response", "$jsonObject")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                Log.d("error", "$t")
            }
        })
    }

    fun showStates() {

        var statesArrayList = ArrayList<String>()
        for (i in 0 until stateList!!.length()) {
            val tempState = stateList!!.getJSONObject(i)
            statesArrayList.add(tempState.getString("state_name"))
        }
        nursery_state.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)
        nursery_state.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getDistricts()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
    fun getDistricts() {
        val roleParams = HashMap<String, String>()

        val selectedState = stateList!!.getJSONObject(nursery_state.selectedItemPosition)
        roleParams["state_id"] = selectedState.getString("state_id")


        val call: Call<ResponseBody> = ApiClient.getClient.districtList(roleParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                Log.d("response", "$jsonObject")
                val status = jsonObject.getInt("status")
                if (status == 1) {
                    districtList = jsonObject.getJSONArray("district_list")
                    showDistricts()
                } else {
                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()
                    districtList = JSONArray("[]")
                    showDistricts()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showDistricts() {

        var statesArrayList = ArrayList<String>()
        for (i in 0 until districtList!!.length()) {
            val tempState = districtList!!.getJSONObject(i)
            statesArrayList.add(tempState.getString("district_name"))
        }
        nursery_district.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)

    }


    private fun addNursery(){
        val kProgressHUD = Singleton().createLoading(this,"Loading","")
        val userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        val userId = userObject.getString("user_id")
        val roleId = userObject.getString("role_id")

        val selectedStatePos = nursery_state.selectedItemPosition
        val selectedDistrictPos = nursery_district.selectedItemPosition

        val selectedStateId = stateList!!.getJSONObject(selectedStatePos).getString("state_id")
        val selectedDisctrictId = districtList!!.getJSONObject(selectedDistrictPos).getString("district_id")


        val addNurseryParams = HashMap<String,String>()
        addNurseryParams["user_id"] = userId
        addNurseryParams["user_role_id"]= roleId
        addNurseryParams["nursery_name"]= et_nurseryName.text.toString()
        addNurseryParams["nursery_address"]= et_nurseryAddress.text.toString()
        addNurseryParams["state_id"]= selectedStateId.toString()
        addNurseryParams["district_id"]= selectedDisctrictId.toString()
        addNurseryParams["nursery_owner"] = et_nurseryOwner.text.toString()
        addNurseryParams["nursery_status"]= "1"

        val call:Call<ResponseBody> = ApiClient.getClient.addNursery(addNurseryParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                kProgressHUD?.dismiss()
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                if (status == 1) {
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    intent = Intent(this@AddNurseryActivity,NurseryListActivity::class.java)
                    startActivity(intent)
                    this@AddNurseryActivity.finish()
                }
                else{
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                kProgressHUD?.dismiss()
            }
        })
    }
}