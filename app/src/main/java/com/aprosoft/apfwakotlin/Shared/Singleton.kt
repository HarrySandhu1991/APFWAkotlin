package com.aprosoft.apfwakotlin.Shared

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.kaopiz.kprogresshud.KProgressHUD
import org.json.JSONObject

class Singleton {

    val prefName ="apfwaPref"
    val userPref = "UserPref"

    fun setSharedPrefrence(context: Context, jsonObject: JSONObject){
        val sharedPreferences: SharedPreferences =context.getSharedPreferences(
            prefName,
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString(userPref, jsonObject.toString()).apply()
    }

    fun getUserFromSharedPrefrence(context: Context): JSONObject?{
        val sharedPreferences: SharedPreferences =context.getSharedPreferences(
            prefName,
            Context.MODE_PRIVATE
        )
        return if (sharedPreferences.contains(userPref)){
            val userString =sharedPreferences.getString(userPref, "")
            if (userString.equals("", true)){
                null
            }else{
                JSONObject(userString)
            }
        }else{
            null
        }
    }

    fun getUserIdFromSavedUser(context: Context) : String {
        val userJSONObject = getUserFromSharedPrefrence(context)
        if (userJSONObject != null) {
            if (userJSONObject.has("farmer_id")) {
                return userJSONObject.getString("farmer_id")
            } else if (userJSONObject.has("user_id"))
                return userJSONObject.getString("user_id")
            else
                return ""
        } else {
            return ""
        }
    }

    fun getUserRoleFromSavedUser(context: Context) :String {
        val userJSONObject = getUserFromSharedPrefrence(context)
        if (userJSONObject != null) {
            if (userJSONObject.has("role_id"))
                return userJSONObject.getString("role_id")
            else
                return ""
        } else {
            return ""
        }
    }

    fun removeUserFromPreferences(context: Context) : Boolean {
        val sharedPreferences: SharedPreferences =context.getSharedPreferences(
                prefName,
                Context.MODE_PRIVATE
        )
        if (sharedPreferences.contains(userPref)) {
            sharedPreferences.edit().remove(userPref).apply()
            return true
        }
        return false
    }

    fun createLoading(context: Context?, title: String?, message: String?): KProgressHUD? {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(title)
            .setDetailsLabel(message)
            .setCancellable(false)
            .setAnimationSpeed(3)
            .setDimAmount(0.4f)
            .show()
    }


}