package com.aprosoft.apfwakotlin.Farmers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_add_farmer.*
import kotlinx.android.synthetic.main.activity_farmer_information.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FarmerInformationActivity : AppCompatActivity() {

    var userObject : JSONObject? = null
    var selectedStateId = ""
    var selectedDistrictId = ""
    var stateList:JSONArray? = null
    var districtList:JSONArray? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_information)

        btn_search_farmer.setOnClickListener {
            checkAdhaarCard()
        }

        btn_receipt.setOnClickListener {
            if (userObject != null) {
                val intent = Intent(this, FarmerRegistrationFormActivity::class.java)
                intent.putExtra("DATA", userObject.toString())
                startActivity(intent)
            }
        }

    }


    fun checkAdhaarCard() {
        val adhaarNo = et_farmer_reg_no.text.toString()
        if (adhaarNo.isBlank() || adhaarNo.length < 12) {
            et_farmer_reg_no.error = "Not a valid input"
            return
        }

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id
        params["farmer_adhar_no"] = adhaarNo

        val call: Call<ResponseBody> = ApiClient.getClient.checkFarmer(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                hideKeyboard()
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                if (status == 1) {
                    val userJsonArray = jsonObject.getJSONArray("farmer_data")
                    if (userJsonArray.length() > 0) {
                        userObject = userJsonArray.getJSONObject(0)
                        et_farmer_name.setText(userObject!!.getString("farmer_name"))
                        et_farmer_address.setText(userObject!!.getString("farmer_address"))
                        et_farmer_mobile.setText(userObject!!.getString("farmer_mobile"))
                        et_farmer_whatsapp.setText(userObject!!.getString("farmer_whatsapp_no"))
                        et_farmer_state.setText(userObject!!.getString("state_name"))
                        et_farmer_district.setText(userObject!!.getString("district_name"))

                        et_dealer_name.setText(userObject!!.getString("dealer_name"))
                        et_dealer_mobile.setText(userObject!!.getString("dealer_mobile"))
                        et_dealer_state.setText(userObject!!.getString("dealer_state"))
                        et_dealer_district.setText(userObject!!.getString("dealer_district"))

                        selectedStateId = userObject!!.getString("state_id")
                        selectedDistrictId = userObject!!.getString("district_id")

//                        et_farmer_state.setText(userObject!!.getString(""))
//                        et_user_name.setText(userObject!!.getString("user_name"))
//                        et_user_address.setText(userObject!!.getString("user_address"))
//                        et_user_mobile.setText(userObject!!.getString("user_mobile"))
//                        et_user_whatsapp.setText(userObject!!.getString("user_whatsapp_no"))
//                        et_user_email.setText(userObject!!.getString("user_email"))
                    } else {
                        val intent = Intent(this@FarmerInformationActivity, AddFarmerActivity::class.java)
                        startActivity(intent)
                    }


                } else {
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@FarmerInformationActivity, AddFarmerActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })

    }


//    fun getStateList() {
//        val call:Call<ResponseBody> = ApiClient.getClient.getStateList()
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                val res = response.body()?.string()
//                val jsonObject = JSONObject(res)
//                val status = jsonObject.getInt("status")
//                var msg: String?=null
//                var imageStatus: String?=null
//                if (status == 1) {
//                    stateList = jsonObject.getJSONArray("state_list")
//                    showStates()
//                }else{
//                    msg = jsonObject.getString("message")
//                }
//                Log.d("response", "$jsonObject")
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
//                Log.d("error", "$t")
//            }
//        })
//    }
//
//    fun showStates() {
//
////        var statesArrayList = ArrayList<String>()
//        for (i in 0 until stateList!!.length()) {
//            val tempState = stateList!!.getJSONObject(i)
//            val tempStateId = tempState.getString("state_id")
//            if (tempStateId == selectedStateId) {
//                et_farmer_state.setText(tempState.getString("state_name"))
//            }
////            statesArrayList.add(tempState.getString("state_name"))
//        }
////        sp_state.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)
////        sp_state.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
////            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
////                getDistricts()
////            }
////
////            override fun onNothingSelected(p0: AdapterView<*>?) {
////
////            }
////        }
//    }
//
//    fun getDistricts() {
//        val roleParams = HashMap<String, String>()
//
//        roleParams["state_id"] = selectedStateId
//
//
//        val call: Call<ResponseBody> = ApiClient.getClient.districtList(roleParams)
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                val res = response.body()?.string()
//                val jsonObject = JSONObject(res)
//                Log.d("response", "$jsonObject")
//                val status = jsonObject.getInt("status")
//                if (status == 1) {
//                    districtList = jsonObject.getJSONArray("district_list")
//                    showDistricts()
//                } else {
//                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()
//                    districtList = JSONArray("[]")
//                    showDistricts()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    fun showDistricts() {
//
//        var statesArrayList = ArrayList<String>()
//        for (i in 0 until districtList!!.length()) {
//            val tempState = districtList!!.getJSONObject(i)
//            val tempDistrictId = tempState.getString("district_id")
//            if (tempDistrictId == selectedDistrictId) {
//                et_farmer_district.setText(tempState.getString("district_name"))
//            }
////            statesArrayList.add(tempState.getString("district_name"))
//        }
////        sp_district.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)
//
//    }


    fun Activity.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_farmer_reg_no.windowToken, 0);
    }
}