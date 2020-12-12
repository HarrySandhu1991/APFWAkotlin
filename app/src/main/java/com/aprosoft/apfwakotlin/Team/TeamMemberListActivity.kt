package com.aprosoft.apfwakotlin.Team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_farmer_list.*
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
    var teamMemberListCopy:JSONArray? = null
    var promoterID:String = ""
    var adapter:TeamMemberListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_member_list)


        promoterID = intent.extras?.getString("P_ID")!!

        et_search_team_member.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(et_search_team_member.text.toString())
            }
        })

        getTeamList()
    }


    fun filterData(string: String) {

        if (teamMemberList == null || teamMemberListCopy == null) {
            return
        }

        if (et_search_team_member.text.toString().isBlank()) {
            teamMemberList = teamMemberListCopy
            adapter!!.notifyChanges(teamMemberList!!)
            return
        }

        teamMemberList = JSONArray()
        for (i in 0 until teamMemberListCopy!!.length()) {
            val tempTeam = teamMemberListCopy!!.getJSONObject(i)

            val user_name = tempTeam.getString("user_name")
            val user_address = tempTeam.getString("user_address")
            val user_mobile = tempTeam.getString("user_mobile")
            val user_email = tempTeam.getString("user_email")
            val user_adhar_no = tempTeam.getString("user_adhar_no")
            val state_name = tempTeam.getString("state_name")
            val district_name = tempTeam.getString("district_name")


            when {
                user_name.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                user_address.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                user_mobile.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                user_email.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                user_adhar_no.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                state_name.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
                district_name.contains(string,true) -> {
                    teamMemberList!!.put(tempTeam)
                }
            }
        }

        if (teamMemberList!!.length() == 0) {
            return
        }

        if (adapter != null) {
            adapter!!.notifyChanges(teamMemberList!!)
        }

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
                    teamMemberListCopy = teamMemberList
                    adapter = TeamMemberListAdapter(this@TeamMemberListActivity,teamMemberList!!)
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