package com.aprosoft.apfwakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.aprosoft.apfwakotlin.Shared.Singleton
import okhttp3.ResponseBody
import retrofit2.Call
import com.aprosoftech.apfwa.Retrofit.ApiClient
import com.kaopiz.kprogresshud.KProgressHUD
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var et_mobileNumber:EditText
    lateinit var et_password:EditText
    lateinit var btn_login:Button
    lateinit var kProgressHUD:KProgressHUD
    lateinit var tv_forgotPassword:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Singleton().getUserFromSharedPrefrence(this)==null){

        }else{
            intent = Intent(this@LoginActivity,AddPromoterActivity::class.java)
            startActivity(intent)
            this@LoginActivity.finish()
        }

        et_mobileNumber= findViewById(R.id.et_mobileNumer)
        et_password= findViewById(R.id.et_password)
        btn_login= findViewById(R.id.btn_login)
        tv_forgotPassword= findViewById(R.id.tv_forgotPassword)

        btn_login.setOnClickListener {

            val mobile = et_mobileNumber.text.toString()
            val password = et_password.text.toString()

            if (mobile.isBlank()) {
                et_mobileNumber.setError("Mobile Number cannot be empty")
                return@setOnClickListener
            }

            if (password.isBlank()) {
                et_password.setError("Password cannot be empty")
                return@setOnClickListener
            }

            login()
        }
        tv_forgotPassword.setOnClickListener {
            intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
//        kProgressHUD= Singleton().createLoading(applicationContext,"Logging in","...")!!

        val loginParams= HashMap<String,String>()
        loginParams["mobile"] = et_mobileNumber.text.toString()
        loginParams["password"] = et_password.text.toString()
        loginParams["device_id"] = "123"

        val call:Call<ResponseBody> = ApiClient.getClient.login(loginParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                kProgressHUD.dismiss()
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                if (status == 1) {
                    message = jsonObject.getString("message")
                    val userDataArray = jsonObject.getJSONArray("user_data")
                    val userDataObject = userDataArray.getJSONObject(0)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    Singleton().setSharedPrefrence(this@LoginActivity,userDataObject)
                    intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                    startActivity(intent)
                    this@LoginActivity.finish()

                }else{
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
//                kProgressHUD.dismiss()
            }
        })
    }
}