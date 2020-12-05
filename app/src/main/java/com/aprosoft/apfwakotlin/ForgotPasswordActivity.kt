package com.aprosoft.apfwakotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.aprosoftech.apfwa.Retrofit.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var et_registeredMobileNumber:EditText
    lateinit var et_registeredEmail:EditText
    lateinit var btn_submit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        et_registeredMobileNumber = findViewById(R.id.et_forgotMobileNumer)
        et_registeredEmail = findViewById(R.id.et_forgotPassword)
        btn_submit= findViewById(R.id.btn_submit)

        btn_submit.setOnClickListener {
            forgotPassword()
        }

    }


    private fun forgotPassword(){

        val forgotParams = HashMap<String,String>()
        forgotParams["mobile"]= et_registeredMobileNumber.text.toString()
        forgotParams["email"]= et_registeredEmail.text.toString()

        val call:Call<ResponseBody> = ApiClient.getClient.forgotPassword(forgotParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String? = null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, "$msg", Toast.LENGTH_SHORT).show()
                }else{
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, "$msg", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }

        })

    }
}