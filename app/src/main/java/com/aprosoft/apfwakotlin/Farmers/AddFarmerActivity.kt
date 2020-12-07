package com.aprosoft.apfwakotlin.Farmers

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Retrofit.RetrofitUtils
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_add_farmer.*
import kotlinx.android.synthetic.main.activity_promoters_information.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddFarmerActivity : AppCompatActivity() {

//    lateinit var et_farmerState:EditText
//    lateinit var et_farmerDistrict:EditText
//    lateinit var et_farmerFullName:EditText
//    lateinit var et_farmerAddress:EditText
//    lateinit var et_farmerMobile:EditText
//    lateinit var et_farmerWhatsapp:EditText
//    lateinit var et_farmerAdhar:EditText
//    lateinit var et_farmerLand:EditText
//    lateinit var btn_addFarmer:Button
//    lateinit var btn_cancelFarmer:Button
    lateinit var userId:String
    lateinit var roleId:String
    lateinit var userStatus:String
    var profileImage: File? = File("")
    var userObject= JSONObject()
    var stateList:JSONArray? = null
    var districtList:JSONArray? = null

    var isEditing = false
    var selectedFarmer:JSONObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_farmer)

        userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        userId = userObject.getString("user_id")
        roleId = userObject.getString("role_id")
        userStatus = userObject.getString("user_status")

        if (intent.hasExtra("EDIT")) {
            isEditing = intent.extras!!.getBoolean("EDIT")
            if (isEditing) {
                selectedFarmer = JSONObject(intent.extras!!.getString("FARMER"))
                showFarmerData()
            }
        }


//        et_farmerState = findViewById(R.id.et_farmerState)
//        et_farmerDistrict = findViewById(R.id.et_farmerDistrict)
//        et_farmerFullName = findViewById(R.id.et_farmerFullName)
//        et_farmerAddress = findViewById(R.id.et_farmerAddress)
//        et_farmerMobile = findViewById(R.id.et_farmerMobile)
//        et_farmerWhatsapp = findViewById(R.id.et_farmerWhatsapp)
//        et_farmerAdhar = findViewById(R.id.et_farmerAdhar)
//        et_farmerLand= findViewById(R.id.et_farmerLand)
//        btn_addFarmer = findViewById(R.id.btn_AddFarmer)
//        btn_cancelFarmer = findViewById(R.id.btn_farmerCancel)


        ib_farmer_add_image.setOnClickListener {
            selectProfileImage()
        }

        btn_AddFarmer.setOnClickListener {
            validateInput()
        }

        btn_farmerCancel.setOnClickListener {
            this.finish()
        }

        getStateList()

    }

    fun showFarmerData() {
        et_farmerFullName.setText(selectedFarmer?.getString("farmer_name"))
        et_farmerAddress.setText(selectedFarmer?.getString("farmer_address"))
        et_farmerLand.setText(selectedFarmer?.getString("farmer_tot_land"))
        et_farmerMobile.setText(selectedFarmer?.getString("farmer_mobile"))
        et_farmerWhatsapp.setText(selectedFarmer?.getString("farmer_whatsapp_no"))
        et_farmerAdhar.setText(selectedFarmer?.getString("farmer_adhar_no"))
    }

    fun validateInput() {
        val name = et_farmerFullName.text.toString()
        val address = et_farmerAddress.text.toString()
        val land = et_farmerLand.text.toString()
        val mobile = et_farmerMobile.text.toString()
        val whatsapp = et_farmerWhatsapp.text.toString()
        val adhaar = et_farmerAdhar.text.toString()

        if (name.isBlank()) {
            et_farmerFullName.error = "Farmer Name is required"
            return
        }

        if (address.isBlank()) {
            et_farmerAddress.error = "Address is required"
            return
        }

        if (land.isBlank()) {
            et_farmerLand.error = "Land is required"
            return
        }

        if (mobile.isBlank()) {
            et_farmerMobile.error = "Mobile No. is required"
            return
        }

        if (whatsapp.isBlank()) {
            et_farmerWhatsapp.error = "Whatsapp No. is required"
            return
        }

        if (adhaar.isBlank()) {
            et_farmerAdhar.error = "Adhaar No. is required"
            return
        }

        if (adhaar.length < 12) {
            et_farmerAdhar.error = "Adhaar No. is not valid"
            return
        }

        if (isEditing) {
            btn_AddFarmer.setText("Update Farmer")
            updateFarmer()
        } else
            addFarmer()

    }

    fun selectProfileImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                ImagePicker.with(this@AddFarmerActivity)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .start { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            //Image Uri will not be null for RESULT_OK
                            val fileUri = data?.data
//                            imgProfile.setImageURI(fileUri)
                            //You can get File object from intent
                            profileImage = ImagePicker.getFile(data)
                            //You can also get File Path from intent
                            val filePath: String? = ImagePicker.getFilePath(data)
                        } else if (resultCode == ImagePicker.RESULT_ERROR) {
                            Toast.makeText(this@AddFarmerActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AddFarmerActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@AddFarmerActivity,
                    "Permission Denied${deniedPermissions.toString()}".trimIndent(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).check()
    }

    private fun updateFarmer(){


        val selectedStatePos = sp_state.selectedItemPosition
        val selectedDistrictPos = sp_district.selectedItemPosition

        val selectedStateId = stateList!!.getJSONObject(selectedStatePos).getString("state_id")
        val selectedDisctrictId = districtList!!.getJSONObject(selectedDistrictPos).getString("district_id")



        val addFarmerParams = HashMap<String,RequestBody>()
        addFarmerParams["user_id"] = RetrofitUtils.StringtoRequestBody(userId)
        addFarmerParams["user_role_id"]= RetrofitUtils.StringtoRequestBody(roleId)
        addFarmerParams["farmer_id"]= RetrofitUtils.StringtoRequestBody(selectedFarmer!!.getString("farmer_id"))
        addFarmerParams["farmer_name"] = RetrofitUtils.StringtoRequestBody(et_farmerFullName.text.toString())
        addFarmerParams["farmer_address"]= RetrofitUtils.StringtoRequestBody(et_farmerAddress.text.toString())
        addFarmerParams["state_id"]= RetrofitUtils.StringtoRequestBody(selectedStateId)
        addFarmerParams["district_id"] = RetrofitUtils.StringtoRequestBody(selectedDisctrictId)
        addFarmerParams["farmer_mobile"]= RetrofitUtils.StringtoRequestBody(et_farmerMobile.text.toString())
        addFarmerParams["farmer_whatsapp_no"]= RetrofitUtils.StringtoRequestBody(et_farmerWhatsapp.text.toString())
        addFarmerParams["farmer_adhar_no"]= RetrofitUtils.StringtoRequestBody(et_farmerAdhar.text.toString())
        addFarmerParams["farmer_tot_land"] = RetrofitUtils.StringtoRequestBody(et_farmerLand.text.toString())
        addFarmerParams["farmer_status"] = RetrofitUtils.StringtoRequestBody(userStatus)

        val image: RequestBody = RetrofitUtils.fileToRequestBody(profileImage)
        val profilePic: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", profileImage!!.getName(), image)


        val call:Call<ResponseBody> = ApiClient.getClient.updateFarmer(profilePic,addFarmerParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    imageStatus = jsonObject.getString("image_status")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                }else{
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
                Log.d("response", "$jsonObject")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                Log.d("error", "$t")
            }
        })

    }

    private fun addFarmer(){

        val selectedStatePos = sp_state.selectedItemPosition
        val selectedDistrictPos = sp_district.selectedItemPosition

        val selectedStateId = stateList!!.getJSONObject(selectedStatePos).getString("state_id")
        val selectedDisctrictId = districtList!!.getJSONObject(selectedDistrictPos).getString("district_id")



        val addFarmerParams = HashMap<String,RequestBody>()
        addFarmerParams["user_id"] = RetrofitUtils.StringtoRequestBody(userId)
        addFarmerParams["user_role_id"]= RetrofitUtils.StringtoRequestBody(roleId)
        addFarmerParams["farmer_name"] = RetrofitUtils.StringtoRequestBody(et_farmerFullName.text.toString())
        addFarmerParams["farmer_address"]= RetrofitUtils.StringtoRequestBody(et_farmerAddress.text.toString())
        addFarmerParams["state_id"]= RetrofitUtils.StringtoRequestBody(selectedStateId)
        addFarmerParams["district_id"] = RetrofitUtils.StringtoRequestBody(selectedDisctrictId)
        addFarmerParams["farmer_mobile"]= RetrofitUtils.StringtoRequestBody(et_farmerMobile.text.toString())
        addFarmerParams["farmer_whatsapp_no"]= RetrofitUtils.StringtoRequestBody(et_farmerWhatsapp.text.toString())
        addFarmerParams["farmer_adhar_no"]= RetrofitUtils.StringtoRequestBody(et_farmerAdhar.text.toString())
        addFarmerParams["farmer_tot_land"] = RetrofitUtils.StringtoRequestBody(et_farmerLand.text.toString())
        addFarmerParams["farmer_status"] = RetrofitUtils.StringtoRequestBody(userStatus)

        val image: RequestBody = RetrofitUtils.fileToRequestBody(profileImage)
        val profilePic: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", profileImage!!.getName(), image)


        val call:Call<ResponseBody> = ApiClient.getClient.addFarmer(profilePic,addFarmerParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    imageStatus = jsonObject.getString("image_status")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                }else{
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, imageStatus, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
                Log.d("response", "$jsonObject")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
                Log.d("error", "$t")
            }
        })

    }

    fun getStateList() {
        val call:Call<ResponseBody> = ApiClient.getClient.getStateList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    stateList = jsonObject.getJSONArray("state_list")
                    showStates()
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

    fun showStates() {

        var statesArrayList = ArrayList<String>()
        for (i in 0 until stateList!!.length()) {
            val tempState = stateList!!.getJSONObject(i)
            statesArrayList.add(tempState.getString("state_name"))
        }
        sp_state.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)
        sp_state.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getDistricts()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    fun getDistricts() {
        val roleParams = HashMap<String, String>()

        val selectedState = stateList!!.getJSONObject(sp_state.selectedItemPosition)
        roleParams["state_id"] = selectedState.getString("state_id")


        val call: Call<ResponseBody> = ApiClient.getClient.districtList(roleParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                Log.d("response", "$jsonObject")
                val status = jsonObject.getInt("status")
                if (status == 1) {
                    districtList = jsonObject.getJSONArray("district_list")
                    showDistricts()
                } else {
                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()
                    districtList = JSONArray("[]")
                    showDistricts()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showDistricts() {

        var statesArrayList = ArrayList<String>()
        for (i in 0 until districtList!!.length()) {
            val tempState = districtList!!.getJSONObject(i)
            statesArrayList.add(tempState.getString("district_name"))
        }
        sp_district.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,statesArrayList)

    }
}