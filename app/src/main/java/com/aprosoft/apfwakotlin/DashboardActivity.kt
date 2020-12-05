package com.aprosoft.apfwakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout

class DashboardActivity : AppCompatActivity() {
    lateinit var rl_farmersInfromation:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val rl_promoterLogin =findViewById<RelativeLayout>(R.id.rl_promoterLogin)
        rl_farmersInfromation= findViewById(R.id.rl_farmersInfromation)

        rl_promoterLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        rl_farmersInfromation.setOnClickListener {
            intent= Intent(this,AddFarmerActivity::class.java)
            startActivity(intent)
        }


    }
}