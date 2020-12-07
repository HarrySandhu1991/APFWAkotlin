package com.aprosoft.apfwakotlin.Promotional

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Retrofit.RetrofitUtils
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_add_farmer.*
import kotlinx.android.synthetic.main.activity_add_promotional_material.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddPromotionalMaterialActivity : AppCompatActivity() {

    val materialTypes = arrayOf("Image","PDF","Youtube URL")
    var profileImage:File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promotional_material)


        sp_material_type.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,materialTypes)
        sp_material_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 2) {
                    til_youtube_link.visibility = View.VISIBLE
                    ll_images.visibility = View.GONE
                } else {
                    til_youtube_link.visibility = View.GONE
                    ll_images.visibility = View.VISIBLE
                }
            }

        }

        ib_promotional_file.setOnClickListener {
            selectPromotionalFile()
        }



        btn_AddMaterial.setOnClickListener {
            checkAndAdd()
        }

    }

    fun checkAndAdd() {

        val userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject","$userObject")
        val userId = userObject.getString("user_id")
        val roleId = userObject.getString("role_id")

        val selectedPos = sp_material_type.selectedItemPosition
        val youtube_url = et_youtube_link.text
        if (selectedPos == 2) {
            if (youtube_url.isBlank()) {
                et_youtube_link.error = "Enter Video"
                return
            }
        } else {
            if (profileImage == null) {
                Toast.makeText(this,"Select a file",Toast.LENGTH_LONG).show()
                return
            }
            profileImage = null// File("")
        }



        val addFarmerParams = HashMap<String, RequestBody>()
        addFarmerParams["user_id"] = RetrofitUtils.StringtoRequestBody(userId)
        addFarmerParams["user_role_id"]= RetrofitUtils.StringtoRequestBody(roleId)
        addFarmerParams["material_type"] = RetrofitUtils.StringtoRequestBody("${sp_material_type.selectedItemPosition+1}")
        addFarmerParams["material_language"]= RetrofitUtils.StringtoRequestBody(et_materialLanguage.text.toString())
        addFarmerParams["material_title"]= RetrofitUtils.StringtoRequestBody(et_materialTitle.text.toString())
        addFarmerParams["material_youtube_url"]= RetrofitUtils.StringtoRequestBody(et_youtube_link.text.toString())
        addFarmerParams["material_status"]= RetrofitUtils.StringtoRequestBody("1")



        val call: Call<ResponseBody>
        if (profileImage == null) {
            call = ApiClient.getClient.addPromotionalWithoutImage(addFarmerParams)
        } else {
            val image: RequestBody = RetrofitUtils.fileToRequestBody(profileImage)
            val profilePic: MultipartBody.Part =
                MultipartBody.Part.createFormData("material_file", profileImage!!.getName(), image)

            call = ApiClient.getClient.addPromotional(profilePic,addFarmerParams)
        }

//        call = ApiClient.getClient.addPromotional(profilePic,addFarmerParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String?=null
                var imageStatus: String?=null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }else{
                    msg = jsonObject.getString("message")
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


    fun selectPromotionalFile() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                ImagePicker.with(this@AddPromotionalMaterialActivity)
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
                            Toast.makeText(this@AddPromotionalMaterialActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AddPromotionalMaterialActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@AddPromotionalMaterialActivity,
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



}