package com.aprosoft.apfwakotlin

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddPromoterActivity : AppCompatActivity() {
    lateinit var spinner:Spinner
    lateinit var et_promoterName:EditText
    lateinit var et_promoterAddress:EditText
    lateinit var et_promoterMobileNo:EditText
    lateinit var et_promoterWhatsappNo:EditText
    lateinit var et_promoterAdharNo:EditText
    lateinit var et_searchAdharNumber:EditText
    lateinit var et_promoterEmail:EditText
    lateinit var btn_addPromoter:Button
    lateinit var btn_promoterCancel:Button
    lateinit var userId:String
    lateinit var roleId:String
    lateinit var userStatus:String
    var userObject= JSONObject()
    var roleListArray=JSONArray()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promoter)


        userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject", "$userObject")
        userId = userObject.getString("user_id")
        roleId = userObject.getString("role_id")
        userStatus = userObject.getString("user_status")

        spinner = findViewById(R.id.spinner_roleList)
        et_promoterName= findViewById(R.id.et_promoterName)
        et_promoterAddress= findViewById(R.id.et_promoterAddress)
        et_promoterMobileNo= findViewById(R.id.et_promoterMobileNo)
        et_promoterWhatsappNo= findViewById(R.id.et_promoterWhatsappNo)
        et_promoterAdharNo= findViewById(R.id.et_promoterAdharNo)
        et_searchAdharNumber= findViewById(R.id.et_searchAdharNumber)
        et_promoterEmail= findViewById(R.id.et_promoterEmail)
        btn_addPromoter= findViewById(R.id.btn_addPromoter)
        btn_promoterCancel= findViewById(R.id.btn_promoterCancel)


        btn_addPromoter.setOnClickListener {

            addPromoter()
        }
        btn_promoterCancel.setOnClickListener {
            this.finish()
        }

        roleList()



    }

    private fun addPromoter(){
        val promoterParams = HashMap<String, String>()
        promoterParams["user_id"]= userId
        promoterParams["user_role_id"]= userId
        promoterParams["role_id"]= roleId
        promoterParams["user_name"]= et_promoterName.text.toString()
        promoterParams["user_address"] = et_promoterAddress.text.toString()
        promoterParams["user_mobile"]= et_promoterMobileNo.text.toString()
        promoterParams["user_email"]= et_promoterEmail.text.toString()
        promoterParams["user_whatsapp_no"]= et_promoterWhatsappNo.text.toString()
        promoterParams["user_adhar_no"]= et_promoterAdharNo.text.toString()
        promoterParams["profile_image"]
        promoterParams["adhar_image"]
        promoterParams["user_status"]= userStatus

        val call:Call<ResponseBody> = ApiClient.getClient.addPromoter(promoterParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String? = null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                } else {
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun roleList(){
        val roleParams = HashMap<String, String>()
        roleParams["user_id"] = userId
        roleParams["user_role_id"]= roleId

        val call:Call<ResponseBody> = ApiClient.getClient.roleList(roleParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                Log.d("response", "$jsonObject")
                val status = jsonObject.getInt("status")
                if (status == 1) {
                    roleListArray = jsonObject.getJSONArray("role_list")
                    var roleName:String?= null
//                    val roleListAdapter = RoleListAdapter(this@AddPromoterActivity,roleListArray)
//                    for (i in 0 until roleListArray.length()) {
//                        val jsonobject: JSONObject = roleListArray.getJSONObject(i)
//                         roleName= jsonobject.getString("role_name")
//                        spinner.adapter = ArrayAdapter<String>(this@AddPromoterActivity,
//                            android.R.layout.simple_spinner_dropdown_item,roleName)
//                    }

                    //Toast.makeText(SearchActivity.this,response,Toast.LENGTH_SHORT).show();


                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

}