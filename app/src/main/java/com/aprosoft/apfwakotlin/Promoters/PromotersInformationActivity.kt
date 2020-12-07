package com.aprosoft.apfwakotlin.Promoters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_promoters_information.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromotersInformationActivity : AppCompatActivity() {

    var roleListArray: JSONArray? = null
    var role_id = ""
    var userObject : JSONObject? = null
    var imageBaseUrl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promoters_information)

        roleList()

        btn_search.setOnClickListener {
            checkAdhaarCard()
        }

        btn_show_id_card.setOnClickListener {
            if (userObject != null) {
                val intent = Intent(this,PromoterIdCardActivity::class.java)
                intent.putExtra("PROMOTER",userObject.toString())
                intent.putExtra("ROLE",findRoleNameBasedOnRoleId())
                intent.putExtra("IMG",imageBaseUrl)
                startActivity(intent)
            }
        }

    }

    fun Activity.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_promoter_adhaar_no.windowToken, 0);
    }

    private fun roleList() {
        val roleParams = HashMap<String, String>()

        val user_id = Singleton().getUserIdFromSavedUser(this)
        val role_id = Singleton().getUserRoleFromSavedUser(this)

        roleParams["user_id"] = user_id
        roleParams["user_role_id"] = role_id

        val call: Call<ResponseBody> = ApiClient.getClient.roleList(roleParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                Log.d("response", "$jsonObject")
                val status = jsonObject.getInt("status")
                if (status == 1) {
                    roleListArray = jsonObject.getJSONArray("role_list")
                    val roleName = findRoleNameBasedOnRoleId()
                    if (roleName != "") {
                        et_team_roll_name.setText(roleName)
                    }
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


    fun checkAdhaarCard() {
        val adhaarNo = et_promoter_adhaar_no.text.toString()
        if (adhaarNo.isBlank()) {
            et_promoter_adhaar_no.error = "Enter Adhaar no."
            return
        }

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id
        params["user_adhar_no"] = adhaarNo

        val call: Call<ResponseBody> = ApiClient.getClient.checkPromoterUser(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                hideKeyboard()
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                imageBaseUrl = jsonObject.getString("image_path")
                if (status == 1) {
                    val userJsonArray = jsonObject.getJSONArray("user_data")
                    if (userJsonArray.length() > 0) {
                        userObject = userJsonArray.getJSONObject(0)
                        et_user_name.setText(userObject!!.getString("user_name"))
                        et_user_address.setText(userObject!!.getString("user_address"))
                        et_user_mobile.setText(userObject!!.getString("user_mobile"))
                        et_user_whatsapp.setText(userObject!!.getString("user_whatsapp_no"))
                        et_user_email.setText(userObject!!.getString("user_email"))
                        this@PromotersInformationActivity.role_id = userObject!!.getString("role_id")
                        val roleName = findRoleNameBasedOnRoleId()
                        if (roleName != "") {
                            et_team_roll_name.setText(roleName)
                        }
                    } else {
                        val intent = Intent(this@PromotersInformationActivity,AddPromoterActivity::class.java)
                        startActivity(intent)
                    }


                } else {
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PromotersInformationActivity,AddPromoterActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })

    }


    fun findRoleNameBasedOnRoleId(): String {
        if (roleListArray != null) {
            if (role_id != "") {
                var roleIndex = -1
                for (i in 0 until roleListArray!!.length()) {
                    val roleJsonObject = roleListArray!!.getJSONObject(i)
                    val tempRoleId = roleJsonObject.getString("role_id")
                    if (tempRoleId == role_id) {
                        roleIndex = i
                    }
                }

                if (roleIndex != -1) {
                    val roleJsonObject = roleListArray!!.getJSONObject(roleIndex)
                    return roleJsonObject.getString("role_name")
                }


            }
        }
        return ""
    }
}