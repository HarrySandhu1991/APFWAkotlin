package com.aprosoft.apfwakotlin.Contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        btn_send.setOnClickListener {
            val name = et_contact_name.text
            val email = et_contact_email.text
            val mobile = et_contact_mobile.text
            val message = et_contact_message.text

            if (name != null) {
                if (name.isBlank()) {
                    et_contact_name.error = "Name is required"
                    return@setOnClickListener
                }
            }
            if (email != null) {
                if (email.isBlank()) {
                    et_contact_email.error = "Email is required"
                    return@setOnClickListener
                }
            }
            if (mobile != null) {
                if (mobile.isBlank()) {
                    et_contact_mobile.error = "Mobile No is required"
                    return@setOnClickListener
                }
            }
            if (message != null) {
                if (message.isBlank()) {
                    et_contact_message.error = "Message is required"
                    return@setOnClickListener
                }
            }
            sendContact()
        }

    }


    fun sendContact() {
        val name = et_contact_name.text
        val email = et_contact_email.text
        val mobile = et_contact_mobile.text
        val message = et_contact_message.text

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id
        params["name"] = name.toString()
        params["email"] = email.toString()
        params["mobile"] = mobile.toString()
        params["message"] = message.toString()


        val call: Call<ResponseBody> = ApiClient.getClient.contactUs(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    Toast.makeText(this@ContactUsActivity,msg,Toast.LENGTH_LONG).show()
                    this@ContactUsActivity.finish()
                }else{
                    msg = jsonObject.getString("message")
                    Toast.makeText(this@ContactUsActivity,msg,Toast.LENGTH_LONG).show()
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