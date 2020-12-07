package com.aprosoft.apfwakotlin.Nursery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_nursery_list.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NurseryListActivity : AppCompatActivity() {


    var nurseryListArray:JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_list)

        getNurseryList()
    }



    fun getNurseryList() {
        val call:Call<ResponseBody> = ApiClient.getClient.nurseryList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    nurseryListArray = jsonObject.getJSONArray("nursery_list")
                    val adapter = NurseryListAdapter(this@NurseryListActivity,nurseryListArray!!)
                    lv_nursery_list.adapter = adapter
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

}