package com.aprosoft.apfwakotlin.Team

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.Farmers.Admin.FarmerListActivity
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_my_team.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTeamActivity : AppCompatActivity() {

    var teamListArray:JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_team)


        btn_farmer_list.setOnClickListener {
            val intent = Intent(this, FarmerListActivity::class.java)
            startActivity(intent)
        }

        getTeamList()
    }




    fun getTeamList() {

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id

        val call: Call<ResponseBody> = ApiClient.getClient.getTeamList(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    teamListArray = jsonObject.getJSONArray("team_counter_list")
                    val adapter = TeamListAdapte(this@MyTeamActivity,teamListArray!!)
                    lv_team_list.adapter = adapter
                    lv_team_list.setOnItemClickListener { adapterView, view, i, l ->
                        val tempTeam = teamListArray!!.getJSONObject(i)
                        val intent = Intent(this@MyTeamActivity,TeamMemberListActivity::class.java)
                        intent.putExtra("P_ID",tempTeam.getString("role_id"))
                        startActivity(intent)
                    }
                } else {
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