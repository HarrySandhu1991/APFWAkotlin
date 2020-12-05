package com.aprosoft.apfwakotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aprosoftech.apfwa.Retrofit.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call

class NurseryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_list)

    }



    private fun nurseryList(){
        val call:Call<ResponseBody> = ApiClient.getClient.nurseryList()

    }
}