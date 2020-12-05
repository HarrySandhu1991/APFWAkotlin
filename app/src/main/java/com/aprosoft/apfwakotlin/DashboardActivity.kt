package com.aprosoft.apfwakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout

class DashboardActivity : AppCompatActivity() {
    lateinit var rl_farmersInfromation:RelativeLayout
    lateinit var rl_prmoter_info:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        rl_prmoter_info = findViewById<RelativeLayout>(R.id.rl_prmoter_info)
        rl_farmersInfromation= findViewById(R.id.rl_farmersInfromation)

        rl_prmoter_info.setOnClickListener {
            intent = Intent(this, PromotersInformationActivity::class.java)
            startActivity(intent)
        }
        rl_farmersInfromation.setOnClickListener {
            intent= Intent(this,AddFarmerActivity::class.java)
            startActivity(intent)
        }


    }
}