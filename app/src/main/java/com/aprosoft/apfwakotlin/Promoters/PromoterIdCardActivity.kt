package com.aprosoft.apfwakotlin.Promoters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aprosoft.apfwakotlin.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_promoter_id_card.*
import org.json.JSONObject

class PromoterIdCardActivity : AppCompatActivity() {

    var userObject : JSONObject? = null
    var imageBaseUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promoter_id_card)


        if (intent.hasExtra("PROMOTER")) {
            val userString = intent.extras?.getString("PROMOTER")
            val userRoleString = intent.extras?.getString("ROLE")
            imageBaseUrl = intent.extras?.getString("IMG")!!
            userObject = JSONObject(userString)
            showUserData(userRoleString!!)
        }

    }

    fun showUserData(role:String) {
        tv_user_name.text = userObject!!.getString("user_name")
        tv_user_designation.text = role
        tv_user_address.text = userObject!!.getString("user_address")
        tv_user_phone.text = userObject!!.getString("user_mobile")
        tv_user_adhaar.text = userObject!!.getString("user_adhar_no")

        Glide.with(this).load("$imageBaseUrl${userObject!!.getString("user_image")}").into(iv_promoter_image)
    }
}