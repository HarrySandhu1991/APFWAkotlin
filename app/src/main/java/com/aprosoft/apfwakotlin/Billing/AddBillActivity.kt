package com.aprosoft.apfwakotlin.Billing

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aprosoft.apfwakotlin.Farmers.AddFarmerActivity
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Retrofit.RetrofitUtils
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_add_bill.*
import kotlinx.android.synthetic.main.activity_add_bill.et_farmerAddress
import kotlinx.android.synthetic.main.activity_add_bill.ib_farmer_add_image
import kotlinx.android.synthetic.main.activity_add_farmer.*
import kotlinx.android.synthetic.main.activity_farmer_information.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class AddBillActivity : AppCompatActivity() {

    var userObject:JSONObject? = null
    var farmerObject:JSONObject? = null
    var profileImage: File? = null

    lateinit var userId:String
    lateinit var roleId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bill)

        et_bill_date.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)


            val datepickerdialog: DatePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                et_bill_date.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
            }, y, m, d)

            datepickerdialog.show()
        }

        btn_search.setOnClickListener {
            fetchDetails()
        }

        ib_farmer_add_image.setOnClickListener {
            selectProfileImage()
        }

        btn_AddBill.setOnClickListener {
            addBill()
        }



    }

    fun fetchDetails() {
        val adhaarNo = et_farmer_search.text.toString()
        if (adhaarNo.isBlank()) {
            et_farmer_search.error = "Not a valid input"
            return
        }

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        val params = HashMap<String, String>()
        params["user_id"] = user_id
        params["user_role_id"] = role_id
        params["farmer_reg_no"] = adhaarNo

        val call: Call<ResponseBody> = ApiClient.getClient.searchFarmer(params)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var message: String? = null
                if (status == 1) {
                    val userJsonArray = jsonObject.getJSONArray("farmer_data")
                    if (userJsonArray.length() > 0) {
                        farmerObject = userJsonArray.getJSONObject(0)
                        et_farmerName.setText(farmerObject!!.getString("farmer_name"))
                        et_farmerAddress.setText(farmerObject!!.getString("farmer_address"))
                    } else {
                        val intent = Intent(this@AddBillActivity, AddFarmerActivity::class.java)
                        startActivity(intent)
                    }


                } else {
                    message = jsonObject.getString("message")
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddBillActivity, AddFarmerActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun selectProfileImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                ImagePicker.with(this@AddBillActivity)
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
                            Toast.makeText(this@AddBillActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AddBillActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@AddBillActivity,
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


    fun addBill() {

        userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        userId = userObject!!.getString("user_id")
        roleId = userObject!!.getString("role_id")

        val billNo = et_bill_no.text.toString()
        val billDate = et_bill_date.text.toString()
        val particulars = et_bill_particulars.text.toString()
        val amount = et_total_amount.text.toString()

        if (billNo.isBlank()) {
            et_bill_no.error = "Bill No is required"
            return
        }
        if (billDate.isBlank()) {
            et_bill_date.error = "Bill Date is required"
            return
        }
        if (particulars.isBlank()) {
            et_bill_particulars.error = "Bill Particulars is required"
            return
        }
        if (amount.isBlank()) {
            et_total_amount.error = "Bill Amount is required"
            return
        }

        if (farmerObject == null) {
            et_farmer_search.error = "Farmer is not searched"
            return
        }

        if (profileImage == null) {
            Toast.makeText(this,"Profile Image not selected",Toast.LENGTH_LONG).show()
            return
        }


        val addFarmerParams = HashMap<String, RequestBody>()
        addFarmerParams["user_id"] = RetrofitUtils.StringtoRequestBody(userId)
        addFarmerParams["user_role_id"]= RetrofitUtils.StringtoRequestBody(roleId)
        addFarmerParams["bill_date"] = RetrofitUtils.StringtoRequestBody(et_bill_date.text.toString())
        addFarmerParams["farmer_reg_no"]= RetrofitUtils.StringtoRequestBody(farmerObject!!.getString("farmer_reg_no"))
        addFarmerParams["farmer_id"]= RetrofitUtils.StringtoRequestBody(farmerObject!!.getString("farmer_id"))
        addFarmerParams["farmer_name"]= RetrofitUtils.StringtoRequestBody(farmerObject!!.getString("farmer_name"))
        addFarmerParams["farmer_address"]= RetrofitUtils.StringtoRequestBody(farmerObject!!.getString("farmer_address"))
        addFarmerParams["bill_particular"] = RetrofitUtils.StringtoRequestBody(et_bill_particulars.text.toString())
        addFarmerParams["bill_net_amount"] = RetrofitUtils.StringtoRequestBody(et_total_amount.text.toString())

        val image: RequestBody = RetrofitUtils.fileToRequestBody(profileImage)
        val profilePic: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", profileImage!!.getName(), image)


        val call:Call<ResponseBody> = ApiClient.getClient.addBill(profilePic,addFarmerParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
//                    imageStatus = jsonObject.getString("image_status")
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
}