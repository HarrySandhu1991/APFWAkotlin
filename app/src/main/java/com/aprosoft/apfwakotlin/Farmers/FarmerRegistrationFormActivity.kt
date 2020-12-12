package com.aprosoft.apfwakotlin.Farmers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aprosoft.apfwakotlin.R
import kotlinx.android.synthetic.main.activity_farmer_registration_form.*
import org.json.JSONObject

class FarmerRegistrationFormActivity : AppCompatActivity() {
    var farmerObject:JSONObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_registration_form)

        val stringData = intent.extras?.getString("DATA")
        farmerObject = JSONObject(stringData)



        tv_membership_no.text = farmerObject!!.getString("farmer_reg_no")
        tv_received_from.text = farmerObject!!.getString("farmer_name")
        tv_adhaar_no.text = farmerObject!!.getString("farmer_adhar_no")
        if (farmerObject!!.has("dealer_name"))
            tv_district_distributor_name.text = farmerObject!!.getString("dealer_name")

        if (farmerObject!!.has("dealer_mobile"))
            tv_district_distributor_number.text = farmerObject!!.getString("dealer_mobile")
    }
}