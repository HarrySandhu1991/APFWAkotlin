package com.aprosoft.apfwakotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFarmerActivity : AppCompatActivity() {

    lateinit var et_farmerState:EditText
    lateinit var et_farmerDistrict:EditText
    lateinit var et_farmerFullName:EditText
    lateinit var et_farmerAddress:EditText
    lateinit var et_farmerMobile:EditText
    lateinit var et_farmerWhatsapp:EditText
    lateinit var et_farmerAdhar:EditText
    lateinit var et_farmerLand:EditText
    lateinit var btn_addFarmer:Button
    lateinit var btn_cancelFarmer:Button
    lateinit var userId:String
    lateinit var roleId:String
    lateinit var userStatus:String
    var userObject= JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_farmer)

        userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        userId = userObject.getString("user_id")
        roleId = userObject.getString("role_id")
        userStatus = userObject.getString("user_status")

        et_farmerState = findViewById(R.id.et_farmerState)
        et_farmerDistrict = findViewById(R.id.et_farmerDistrict)
        et_farmerFullName = findViewById(R.id.et_farmerFullName)
        et_farmerAddress = findViewById(R.id.et_farmerAddress)
        et_farmerMobile = findViewById(R.id.et_farmerMobile)
        et_farmerWhatsapp = findViewById(R.id.et_farmerWhatsapp)
        et_farmerAdhar = findViewById(R.id.et_farmerAdhar)
        et_farmerLand= findViewById(R.id.et_farmerLand)
        btn_addFarmer = findViewById(R.id.btn_AddFarmer)
        btn_cancelFarmer = findViewById(R.id.btn_farmerCancel)

        btn_addFarmer.setOnClickListener {
            addFarmer()
        }

        btn_cancelFarmer.setOnClickListener {
            this.finish()
        }

    }



    private fun addFarmer(){
        val addFarmerParams = HashMap<String,String>()
        addFarmerParams["user_id"] = userId
        addFarmerParams["user_role_id"]= roleId
        addFarmerParams["farmer_name"] = et_farmerFullName.text.toString()
        addFarmerParams["farmer_address"]= et_farmerAddress.text.toString()
        addFarmerParams["state_id"]= et_farmerState.text.toString()
        addFarmerParams["district_id"] = et_farmerDistrict.text.toString()
        addFarmerParams["farmer_mobile"]= et_farmerMobile.text.toString()
        addFarmerParams["farmer_whatsapp_no"]= et_farmerWhatsapp.text.toString()
        addFarmerParams["farmer_adhar_no"]= et_farmerAdhar.text.toString()
        addFarmerParams["image"]
        addFarmerParams["farmer_tot_land"] = et_farmerLand.text.toString()
        addFarmerParams["farmer_status"] = userStatus

        val call:Call<ResponseBody> = ApiClient.getClient.addFarmer(addFarmerParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    imageStatus = jsonObject.getString("image_status")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                }else{
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
                Log.d("response", "$jsonObject")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                Log.d("error", "$t")
            }
        })

    }
}