package com.aprosoft.apfwakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_promoters_information.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromotersInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promoters_information)


        btn_search.setOnClickListener {
            checkAdhaarCard()
        }

    }

    fun checkAdhaarCard() {
        val adhaarNo = et_promoter_adhaar_no.text.toString()
        if (adhaarNo.isBlank()) {
            et_promoter_adhaar_no.setError("Enter Adhaar no.")
            return
        }

        val user_id = Singleton().getUserIdFromSavedUser(this)
        val role_id = Singleton().getUserRoleFromSavedUser(this)

        val loginParams= HashMap<String,String>()
        loginParams["user_id"] = user_id
        loginParams["user_role_id"] = role_id
        loginParams["user_adhar_no"] = adhaarNo

        val call: Call<ResponseBody> = ApiClient.getClient.login(loginParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                if (status == 1) {


                }else{
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })

    }
}