package com.aprosoftech.apfwa.Retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST("Mobile_App/User_API/login")
    fun login(@FieldMap params:HashMap<String,String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/User_API/forgot_password")
    fun forgotPassword(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Master_API/add_promoter_user")
    fun addPromoter(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Farmer_API/add_farmer")
    fun addFarmer(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @GET("Mobile_App/Master_API/nursery_list")
    fun nurseryList():Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Master_API/get_role_list")
    fun roleList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>




}