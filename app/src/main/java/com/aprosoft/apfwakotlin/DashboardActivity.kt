package com.aprosoft.apfwakotlin

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aprosoft.apfwakotlin.Adapter.PromotionalAdapter
import com.aprosoft.apfwakotlin.Billing.AddBillActivity
import com.aprosoft.apfwakotlin.Billing.BillingListActivity
import com.aprosoft.apfwakotlin.Contact.ContactUsActivity
import com.aprosoft.apfwakotlin.Farmers.Admin.FarmerListActivity
import com.aprosoft.apfwakotlin.Farmers.FarmerInformationActivity
import com.aprosoft.apfwakotlin.Nursery.NurseryListActivity
import com.aprosoft.apfwakotlin.Promoters.PromotersInformationActivity
import com.aprosoft.apfwakotlin.Promotional.AddPromotionalMaterialActivity
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoft.apfwakotlin.Team.MyTeamActivity
import com.aprosoftech.apfwa.Retrofit.ApiClient
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    var categoryArray: JSONArray? = null
    var categoryDisplayArray: ArrayList<String>? = null
    var categoryViews: ArrayList<View>? = null
    var showingCategories:JSONArray? = null
    var promotionalImageBaseURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        if (role_id != "5" && role_id != "1") {
            rl_billing_info.visibility = View.GONE
        }
        if (role_id == "1") {
            ll_bottom.visibility = View.VISIBLE
        }
        if (role_id == "farmer") {
            rl_prmoter_info.visibility = View.GONE
            rl_billing_info.visibility = View.VISIBLE
            rl_my_team.visibility = View.GONE

        }

        btn_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Confirmation")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to logout?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                Singleton().removeUserFromPreferences(this)
                val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        rl_prmoter_info.setOnClickListener {
            val intent = Intent(this, PromotersInformationActivity::class.java)
            startActivity(intent)
        }

        rl_farmersInfromation.setOnClickListener {
//            var role_id = Singleton().getUserRoleFromSavedUser(this)
//            if (role_id == "3" || role_id == "1") {
//                val intent = Intent(this, FarmerListActivity::class.java)
//                startActivity(intent)
//            } else {
                val intent = Intent(this, FarmerInformationActivity::class.java)
                startActivity(intent)
//            }
        }

        rl_nursery_info.setOnClickListener {
            val intent = Intent(this, NurseryListActivity::class.java)
            startActivity(intent)
        }

        rl_my_team.setOnClickListener {
            val intent = Intent(this, MyTeamActivity::class.java)
            startActivity(intent)
        }

        rl_billing_info.setOnClickListener {
            val intent = Intent(this, BillingListActivity::class.java)
            startActivity(intent)
        }

        rl_contact_us.setOnClickListener {
            val intent = Intent(this, ContactUsActivity::class.java)
            startActivity(intent)
        }

        rl_promotional_content.setOnClickListener {
            val intent = Intent(this, AddPromotionalMaterialActivity::class.java)
            startActivity(intent)
        }

        getDashboardCounters()
        getPromotionalContent()

    }


    fun getDashboardCounters() {
        var user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        if (role_id == "farmer")
            user_id = "1"

        val params = HashMap<String, String>()
        params["user_id"] = user_id
//        params["user_role_id"] = role_id



        val call: Call<ResponseBody> = ApiClient.getClient.getDashboardCounters(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String? = null
                var imageStatus: String? = null
                if (status == 1) {
                    val count = jsonObject.getInt("total_count")
                    if ("$count".length >= 1) {
                        val ones = count % 10
                        tv_ones.text = "$ones".toCharArray()[0].toString()
                    }
                    if ("$count".length >= 2) {
                        val ones = count % 100
                        tv_tens.text = "$ones".toCharArray()[0].toString()
                    }
                    if ("$count".length >= 3) {
                        val ones = count % 1000
                        tv_hundreds.text = "$ones".toCharArray()[0].toString()
                    }
                    if ("$count".length >= 4) {
                        val ones = count % 10000
                        tv_thousand.text = "$ones".toCharArray()[0].toString()
                    }
                    if ("$count".length >= 5) {
                        val ones = count % 100000
                        tv_ten_thousand.text = "$ones".toCharArray()[0].toString()
                    }
                    if ("$count".length >= 5) {
                        val ones = count % 1000000
                        tv_lac.text = "$ones".toCharArray()[0].toString()
                    }

                    tv_registered_members_count.text = jsonObject.getString("total_count")
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

    fun getPromotionalContent() {

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id


        val call: Call<ResponseBody> = ApiClient.getClient.getPromotionalContent(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                if (jsonObject.has("image_path"))
                    promotionalImageBaseURL = jsonObject.getString("image_path")
//                var msg: String? = null
                if (status == 1) {
//                    msg = jsonObject.getString("message")
                    categoryArray = jsonObject.getJSONArray("promotion_material_list")
                    categoryDisplayArray = ArrayList()
                    for (i in 0 until categoryArray!!.length()) {
                        val tempCatArray = categoryArray!!.getJSONObject(i)
                        val materialLanguage = tempCatArray.getString("material_language")
                        val contains = categoryDisplayContains(materialLanguage)
                        if (!contains)
                            categoryDisplayArray!!.add(tempCatArray.getString("material_language"))
                    }
                    showCategories()
                } else {
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

    fun categoryDisplayContains(str:String):Boolean {
        var contains = false
        for (i in 0 until categoryDisplayArray!!.size) {
            val tempDisplay = categoryDisplayArray!!.get(i)
            if (tempDisplay.equals(str,true))
                contains = true
        }
        return contains
    }

    fun showCategories() {
        val layoutInflater = getSystemService(
            LAYOUT_INFLATER_SERVICE
        ) as? LayoutInflater

        categoryViews = ArrayList<View>()

        for (i in 0 until categoryDisplayArray!!.size) {
            val view: View = layoutInflater!!.inflate(R.layout.custom_category, null)
            val tv_cat_name = view.findViewById<TextView>(R.id.tv_cat_name)
            val catname = categoryDisplayArray!!.get(i)
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                view.background = getDrawable(R.drawable.custom_textview_border)
//            } else {
//                view.background = resources.getDrawable(R.drawable.custom_textview_border)
//            }
            tv_cat_name.text = catname
            view.tag = i
            view.setOnClickListener { view ->
                val position = view.tag.toString().toInt()
                markSelected(position)
                try {
                    val string = categoryDisplayArray!!.get(position)
                    showDataAccordingToSelection(string)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            categoryViews!!.add(view)
            ll_categories.addView(view)
        }
        if (categoryDisplayArray!!.size > 0)
            categoryViews!!.get(0).performClick()
    }

    fun markSelected(position: Int) {
        unSelectEverything()
        val v = categoryViews!![position]
        val linearLayout = v.findViewById<View>(R.id.ll_main) as LinearLayout
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.background = getDrawable(R.drawable.rounded_green_border)
        } else {
            linearLayout.background = resources.getDrawable(R.drawable.rounded_green_border)
        }
        val tv_cat_name = v.findViewById<TextView>(R.id.tv_cat_name)
        tv_cat_name.setTextColor(Color.WHITE)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            tv_cat_name.setTextColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.colorPrimary
//                )
//            )
//        } else {
//            tv_cat_name.setTextColor(resources.getColor(R.color.colorPrimary))
//        }
    }

    fun unSelectEverything() {
        for (i in categoryViews!!.indices) {
            val v = categoryViews!![i]
            val linearLayout = v.findViewById<View>(R.id.ll_main) as LinearLayout
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                linearLayout.background = getDrawable(R.drawable.custom_textview_border)
            } else {
                linearLayout.background = resources.getDrawable(R.drawable.custom_textview_border)
            }
            val tv_cat_name = v.findViewById<TextView>(R.id.tv_cat_name)
            tv_cat_name.setTextColor(Color.BLACK)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                tv_cat_name.setTextColor(Color.WHITE)
//            } else {
//                tv_cat_name.setTextColor(Color.WHITE)
//            }
        }
    }


    fun showDataAccordingToSelection(str:String) {
        showingCategories = JSONArray()
        for (i in 0 until categoryArray!!.length()) {
            val tempCat = categoryArray!!.getJSONObject(i)
            if (tempCat.getString("material_language").equals(str,true)) {
                showingCategories!!.put(tempCat)
            }
        }


        val adapter = PromotionalAdapter(this,showingCategories!!,promotionalImageBaseURL)
        lv_promotional_content.adapter = adapter

        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            sv_main.smoothScrollTo(0,0)
        }, 100)


    }
}