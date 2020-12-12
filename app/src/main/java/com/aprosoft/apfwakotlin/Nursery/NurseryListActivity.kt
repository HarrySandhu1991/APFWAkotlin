package com.aprosoft.apfwakotlin.Nursery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
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
    var nurseryListArrayCopy:JSONArray? = null
    var adapter:NurseryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_list)


        val role = Singleton().getUserRoleFromSavedUser(this)
        if (role == "farmer") {
            btn_addNursery.visibility = View.GONE
        }



        getNurseryList()

        btn_addNursery.setOnClickListener {
            intent= Intent(this,AddNurseryActivity::class.java)
            startActivity(intent)
        }


        et_search_nursery.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                Log.d("T2",et_search_nursery.text.toString())
                filterList()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                Log.d("T1",et_search_nursery.text.toString())

            }
        })

    }


    fun filterList() {
        if (nurseryListArrayCopy != null) {

            if (adapter != null && et_search_nursery.text.toString().isBlank()) {
                nurseryListArray = nurseryListArrayCopy
                adapter!!.reloadList(nurseryListArray!!)
                return
            }


            nurseryListArray = JSONArray()
            for (i in 0 until nurseryListArrayCopy!!.length()) {
                val tempNursery = nurseryListArrayCopy!!.getJSONObject(i)
                val name = tempNursery.getString("nursery_name")
                val address = tempNursery.getString("nursery_address")
                val owner = tempNursery.getString("nursery_owner")
                val state = tempNursery.getString("state_name")
                val district = tempNursery.getString("district_name")

                val searchedText = et_search_nursery.text.toString()
                when {
                    name.contains(searchedText, true) -> {
                        nurseryListArray!!.put(tempNursery)
                    }
                    address.contains(searchedText,true) -> {
                        nurseryListArray!!.put(tempNursery)
                    }
                    owner.contains(searchedText,true) -> {
                        nurseryListArray!!.put(tempNursery)
                    }
                    state.contains(searchedText,true) -> {
                        nurseryListArray!!.put(tempNursery)
                    }
                    district.contains(searchedText,true) -> {
                        nurseryListArray!!.put(tempNursery)
                    }
                }


            }
            adapter!!.reloadList(nurseryListArray!!)
        }
    }



    fun getNurseryList() {
        val kProgressHUD = Singleton().createLoading(this,"Loading","")
        val call:Call<ResponseBody> = ApiClient.getClient.nurseryList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                kProgressHUD?.dismiss()
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1 || status == 0) {
                    nurseryListArray = jsonObject.getJSONArray("nursery_list")
                    nurseryListArrayCopy = jsonObject.getJSONArray("nursery_list")
                    adapter = NurseryListAdapter(this@NurseryListActivity,nurseryListArray!!)
                    lv_nursery_list.adapter = adapter
                }else{
//                    msg = jsonObject.getString("message")
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