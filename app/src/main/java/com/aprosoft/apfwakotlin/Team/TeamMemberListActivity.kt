package com.aprosoft.apfwakotlin.Team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_my_team.*
import kotlinx.android.synthetic.main.activity_team_member_list.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamMemberListActivity : AppCompatActivity() {
    var teamMemberList:JSONArray? = null
    var promoterID:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_member_list)


        promoterID = intent.extras?.getString("P_ID")!!

        getTeamList()
    }


    fun getTeamList() {

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id
        params["promoter_role_id"] = promoterID

        val call: Call<ResponseBody> = ApiClient.getClient.getTeamMemberList(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    teamMemberList = jsonObject.getJSONArray("team_member_list")
                    val adapter = TeamMemberListAdapter(this@TeamMemberListActivity,teamMemberList!!)
                    lv_team_member_list.adapter = adapter
                }else{
//                    msg = jsonObject.getString("message")
                }
//                Log.d("response", "$jsonObject")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                Log.d("error", "$t")
            }
        })
    }
}