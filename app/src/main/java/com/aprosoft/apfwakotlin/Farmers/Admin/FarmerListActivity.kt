package com.aprosoft.apfwakotlin.Farmers.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.Farmers.AddFarmerActivity
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoft.apfwakotlin.Team.TeamListAdapte
import com.aprosoft.apfwakotlin.Team.TeamMemberListActivity
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_farmer_list.*
import kotlinx.android.synthetic.main.activity_my_team.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FarmerListActivity : AppCompatActivity() {

    var farmerListArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_list)

    }

    override fun onResume() {
        super.onResume()
        getFarmerList()
    }


    fun getFarmerList() {
        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id

        val call: Call<ResponseBody> = ApiClient.getClient.getFarmerList(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    farmerListArray = jsonObject.getJSONArray("farmer_list")
                    val adapter = FarmerListAdapter(this@FarmerListActivity,farmerListArray!!)
                    lv_farmer_list.adapter = adapter
                    lv_farmer_list.setOnItemClickListener { adapterView, view, i, l ->
                        val selectedFarmer = farmerListArray!!.getJSONObject(i)
                        val intent = Intent(this@FarmerListActivity,AddFarmerActivity::class.java)
                        intent.putExtra("EDIT",true)
                        intent.putExtra("FARMER",selectedFarmer.toString())
                        startActivity(intent)
                    }
//                    lv_team_list.setOnItemClickListener { adapterView, view, i, l ->
//                        val tempTeam = fa!!.getJSONObject(i)
//                        val intent = Intent(this@MyTeamActivity, TeamMemberListActivity::class.java)
//                        intent.putExtra("P_ID",tempTeam.getString("role_id"))
//                        startActivity(intent)
//                    }
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