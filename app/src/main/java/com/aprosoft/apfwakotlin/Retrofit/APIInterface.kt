package com.aprosoftech.apfwa.Retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @FormUrlEncoded
    @POST("Mobile_App/User_API/login")
    fun login(@FieldMap params:HashMap<String,String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/User_API/forgot_password")
    fun forgotPassword(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/User_API/contact_form")
    fun contactUs(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @FormUrlEncoded
    @POST("Mobile_App/User_API/get_total_counters")
    fun getDashboardCounters(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Transaction_API/promotion_material_list")
    fun getPromotionalContent(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

//    @FormUrlEncoded
//    @POST("Mobile_App/Master_API/add_promoter_user")
//    fun addPromoter(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @POST("Mobile_App/Farmer_API/add_farmer")
    @Multipart
    fun addFarmer(@Part profile_image: MultipartBody.Part, @PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>

    @POST("Mobile_App/Farmer_API/update_farmer")
    @Multipart
    fun updateFarmer(@Part profile_image: MultipartBody.Part, @PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>

    @GET("Mobile_App/Master_API/nursery_list")
    fun nurseryList():Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Master_API/get_role_list")
    fun roleList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @GET("Mobile_App/Master_API/state_list")
    fun getStateList():Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Master_API/district_list")
    fun districtList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>




    @FormUrlEncoded
    @POST("Mobile_App/Farmer_API/check_farmer")
    fun checkFarmer(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Farmer_API/search_farmer")
    fun searchFarmer(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @FormUrlEncoded
    @POST("Mobile_App/Master_API/my_team_counter")
    fun getTeamList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @FormUrlEncoded
    @POST("Mobile_App/Master_API/team_member_list")
    fun getTeamMemberList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>



    @FormUrlEncoded
    @POST("Mobile_App/Master_API/check_promoter_user")
    fun checkPromoterUser(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @FormUrlEncoded
    @POST("Mobile_App/Transaction_API/bill_list")
    fun getBillList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @POST("Mobile_App/Master_API/add_promoter_user")
    @Multipart
    fun addPromoter(@Part profile_image: MultipartBody.Part, @Part adhar_image: MultipartBody.Part, @PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>


    @POST("Mobile_App/Transaction_API/add_bill")
    @Multipart
    fun addBill(@Part profile_image: MultipartBody.Part,@PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>





    //ADMIN

    @FormUrlEncoded
    @POST("Mobile_App/Farmer_API/farmer_list")
    fun getFarmerList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>


    @POST("Mobile_App/Transaction_API/add_promotion_material")
    @Multipart
    fun addPromotional(@Part profile_image: MultipartBody.Part, @PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>

    @POST("Mobile_App/Transaction_API/add_promotion_material")
    @Multipart
    fun addPromotionalWithoutImage(@PartMap data:HashMap<String, RequestBody>) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Master_API/add_nursery")
    fun addNursery(@FieldMap params: HashMap<String, String>):Call<ResponseBody>

    @FormUrlEncoded
    @POST("Mobile_App/Transaction_API/nursery_owner_bill_list")
    fun nurseryOwnerBillList(@FieldMap params: HashMap<String, String>):Call<ResponseBody>



}