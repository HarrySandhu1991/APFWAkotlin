package com.aprosoft.apfwakotlin.Promoters

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.aprosoft.apfwakotlin.R
import com.aprosoft.apfwakotlin.Retrofit.RetrofitUtils
import com.aprosoft.apfwakotlin.Shared.Singleton
import com.aprosoftech.apfwa.Retrofit.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_add_promoter.*
import kotlinx.android.synthetic.main.activity_add_promoter.iv_selected_image
import kotlinx.android.synthetic.main.activity_add_promoter.sp_district
import kotlinx.android.synthetic.main.activity_add_promoter.sp_state
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddPromoterActivity : AppCompatActivity() {
//    lateinit var spinner:Spinner
//    lateinit var et_promoterName:EditText
//    lateinit var et_promoterAddress:EditText
//    lateinit var et_promoterMobileNo:EditText
//    lateinit var et_promoterWhatsappNo:EditText
//    lateinit var et_promoterAdharNo:EditText
//    lateinit var et_searchAdharNumber:EditText
//    lateinit var et_promoterEmail:EditText
//    lateinit var btn_addPromoter:Button
//    lateinit var btn_promoterCancel:Button
    lateinit var userId:String
    lateinit var roleId:String
    lateinit var userStatus:String
    var userObject= JSONObject()
    var roleListArray=JSONArray()

    var stateList:JSONArray? = null
    var districtList:JSONArray? = null

    var adhaarImage:File? = File("")
    var profileImage:File? = File("")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promoter)


        userObject = Singleton().getUserFromSharedPrefrence(this)!!
        Log.d("userObject", "$userObject")
        userId = Singleton().getUserIdFromSavedUser(this)
        roleId = Singleton().getUserRoleFromSavedUser(this)
        userStatus = userObject.getString("user_status")


        if (intent.hasExtra("ADHAAR")) {
            val adhaar = intent.extras!!.getString("ADHAAR")
            et_promoterAdharNo.setText(adhaar)
        }

//        spinner = findViewById(R.id.spinner_roleList)
//        et_promoterName= findViewById(R.id.et_promoterName)
//        et_promoterAddress= findViewById(R.id.et_promoterAddress)
//        et_promoterMobileNo= findViewById(R.id.et_promoterMobileNo)
//        et_promoterWhatsappNo= findViewById(R.id.et_promoterWhatsappNo)
//        et_promoterAdharNo= findViewById(R.id.et_promoterAdharNo)
//        et_searchAdharNumber= findViewById(R.id.et_searchAdharNumber)
//        et_promoterEmail= findViewById(R.id.et_promoterEmail)
//        btn_addPromoter= findViewById(R.id.btn_addPromoter)
//        btn_promoterCancel= findViewById(R.id.btn_promoterCancel)

        et_promoterAdharNo.addTextChangedListener(object : TextWatcher {
            private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
            private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
            private val DIVIDER_MODULO =
                    5 // means divider position is every 5th symbol beginning with 1
            private val DIVIDER_POSITION =
                    DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
            private val DIVIDER = '-'
            override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
            ) { // noop
            }

            override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
            ) { // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {

                    var repl = buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER
                    )

                    et_promoterAdharNo.clearFocus();
                    et_promoterAdharNo.setText(repl);
                    et_promoterAdharNo.requestFocus();
                    et_promoterAdharNo.setSelection(repl!!.length);

                }
            }

            private fun isInputCorrect(
                    s: Editable,
                    totalSymbols: Int,
                    dividerModulo: Int,
                    divider: Char
            ): Boolean {
                var isCorrect =
                        s.length <= totalSymbols // check size of entered string
                for (i in 0 until s.length) { // check that every element is right
                    isCorrect = if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect and (divider == s[i])
                    } else {
                        isCorrect and Character.isDigit(s[i])
                    }
                }
                return isCorrect
            }

            private fun buildCorrectString(
                    digits: CharArray,
                    dividerPosition: Int,
                    divider: Char
            ): String? {
                val formatted = StringBuilder()
                for (i in digits.indices) {
                    if (digits[i] != '\u0000') {
                        formatted.append(digits[i])
                        if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                            formatted.append(divider)
                        }
                    }
                }
                return formatted.toString()
            }

            private fun getDigitArray(s: Editable, size: Int): CharArray {
                val digits = CharArray(size)
                var index = 0
                var i = 0
                while (i < s.length && index < size) {
                    val current = s[i]
                    if (Character.isDigit(current)) {
                        digits[index] = current
                        index++
                    }
                    i++
                }
                return digits
            }
        })




        btn_addPromoter.setOnClickListener {

            val promoter_name = et_promoterName.text.toString()
            val promoter_email = et_promoterEmail.text.toString()
            val promoter_address = et_promoterAddress.text.toString()
            val promoter_mobile = et_promoterMobileNo.text.toString()
            val promoter_whatsaapp =  et_promoterWhatsappNo.text.toString()
            val promoter_adhaaar = et_promoterAdharNo.text.toString()


            if (promoter_name.isBlank()) {
                et_promoterName.error = "Name is required"
                return@setOnClickListener
            }
            if (promoter_email.isBlank()) {
                et_promoterEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (promoter_address.isBlank()) {
                et_promoterAddress.error = "Address is required"
                return@setOnClickListener
            }
            if (promoter_mobile.isBlank()) {
                et_promoterMobileNo.error = "Mobile No is required"
                return@setOnClickListener
            }
            if (promoter_whatsaapp.isBlank()) {
                et_promoterWhatsappNo.error = "Whatsapp No is required"
                return@setOnClickListener
            }
            if (promoter_adhaaar.isBlank()) {
                et_promoterAdharNo.error = "Adhaar Number is required"
                return@setOnClickListener
            }





            addPromoter()
        }

        ib_adhaar_card.setOnClickListener {
            selectAdhaarImage()
        }

        ib_profile_image.setOnClickListener {
            selectProfileImage()
        }


        btn_promoterCancel.setOnClickListener {
            this.finish()
        }

        roleList()
        getStateList()



    }

    fun selectProfileImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                ImagePicker.with(this@AddPromoterActivity)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .start { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            //Image Uri will not be null for RESULT_OK
                            val fileUri = data?.data
                            iv_selected_image.setImageURI(fileUri)
                            //You can get File object from intent
                            profileImage = ImagePicker.getFile(data)
                            //You can also get File Path from intent
                            val filePath: String? = ImagePicker.getFilePath(data)
                        } else if (resultCode == ImagePicker.RESULT_ERROR) {
                            Toast.makeText(this@AddPromoterActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AddPromoterActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                        this@AddPromoterActivity,
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

    fun selectAdhaarImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

                ImagePicker.with(this@AddPromoterActivity)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .start { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            //Image Uri will not be null for RESULT_OK
                            val fileUri = data?.data
                            iv_selected_adhaar.setImageURI(fileUri)
                            //You can get File object from intent
                            adhaarImage = ImagePicker.getFile(data)
                            //You can also get File Path from intent
                            val filePath: String? = ImagePicker.getFilePath(data)
                        } else if (resultCode == ImagePicker.RESULT_ERROR) {
                            Toast.makeText(this@AddPromoterActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AddPromoterActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                        this@AddPromoterActivity,
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


    private fun addPromoter(){

        var adhaarNo = et_promoterAdharNo.text.toString()
        if (adhaarNo.isBlank()) {
            et_promoterAdharNo.error = "Adhaar No. is required"
            return
        }

        if (adhaarNo.length < 12 && adhaarNo.length != 14) {
            et_promoterAdharNo.error = "Adhaar No. is not valid"
            return
        }

        val selectedRoleIndex = spinner_roleList.selectedItemPosition
        val tempSelectedRoleObject = roleListArray.getJSONObject(selectedRoleIndex)
        val tempRoleid = tempSelectedRoleObject.getString("role_id")


        val selectedStatePos = sp_state.selectedItemPosition
        val selectedDistrictPos = sp_district.selectedItemPosition

        val selectedStateId = stateList!!.getJSONObject(selectedStatePos).getString("state_id")
        val selectedDisctrictId = districtList!!.getJSONObject(selectedDistrictPos).getString("district_id")



        adhaarNo = adhaarNo.replace("-","")

        val promoterParams = HashMap<String, RequestBody>()
        promoterParams["user_id"]= RetrofitUtils.StringtoRequestBody(userId)
        promoterParams["user_role_id"]= RetrofitUtils.StringtoRequestBody(roleId)
        promoterParams["role_id"]= RetrofitUtils.StringtoRequestBody(tempRoleid)
        promoterParams["user_name"]= RetrofitUtils.StringtoRequestBody(et_promoterName.text.toString())
        promoterParams["user_address"] = RetrofitUtils.StringtoRequestBody(et_promoterAddress.text.toString())
        promoterParams["user_mobile"]= RetrofitUtils.StringtoRequestBody(et_promoterMobileNo.text.toString())
        promoterParams["user_email"]= RetrofitUtils.StringtoRequestBody(et_promoterEmail.text.toString())
        promoterParams["user_whatsapp_no"]= RetrofitUtils.StringtoRequestBody(et_promoterWhatsappNo.text.toString())
        promoterParams["user_adhar_no"]= RetrofitUtils.StringtoRequestBody(adhaarNo)
        promoterParams["user_status"]= RetrofitUtils.StringtoRequestBody("1")
        promoterParams["state_id"]= RetrofitUtils.StringtoRequestBody(selectedStateId)
        promoterParams["district_id"] = RetrofitUtils.StringtoRequestBody(selectedDisctrictId)


        val image: RequestBody = RetrofitUtils.fileToRequestBody(profileImage)
        val profilePic: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", profileImage!!.getName(), image)


        val adhaar_image: RequestBody = RetrofitUtils.fileToRequestBody(adhaarImage)
        val adhaarPic: MultipartBody.Part =
            MultipartBody.Part.createFormData("adhar_image", adhaarImage!!.getName(), adhaar_image)



        val call:Call<ResponseBody> = ApiClient.getClient.addPromoter(profilePic, adhaarPic, promoterParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String? = null
                if (status == 1) {
                    msg = jsonObject.getString("message")
                    val builder = AlertDialog.Builder(this@AddPromoterActivity)
                    //set title for alert dialog
                    builder.setTitle("Success")
                    //set message for alert dialog
                    builder.setMessage(msg)
                    builder.setIcon(android.R.drawable.ic_dialog_info)

                    //performing positive action
                    builder.setPositiveButton("OK") { dialogInterface, which ->
//                        Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                        this@AddPromoterActivity.finish()
                    }
                    //performing cancel action
                    builder.setNeutralButton("Cancel") { dialogInterface, which ->
//                        Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
                    }
                    //performing negative action
//                    builder.setNegativeButton("No"){dialogInterface, which ->
//                        Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
//                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
//                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                } else {
                    msg = jsonObject.getString("message")
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun roleList(){
        val roleParams = HashMap<String, String>()

        val user_id = Singleton().getUserIdFromSavedUser(this)
        var role_id = Singleton().getUserRoleFromSavedUser(this)

        roleParams["user_id"] = user_id
        roleParams["user_role_id"]= role_id

        val call:Call<ResponseBody> = ApiClient.getClient.roleList(roleParams)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                Log.d("response", "$jsonObject")
                val status = jsonObject.getInt("status")
                if (status == 1) {
                    roleListArray = jsonObject.getJSONArray("role_list")
                    populateRoles()
//                    var roleName:String?= null
//                    val roleListAdapter = RoleListAdapter(this@AddPromoterActivity,roleListArray)
//                    for (i in 0 until roleListArray.length()) {
//                        val jsonobject: JSONObject = roleListArray.getJSONObject(i)
//                         roleName= jsonobject.getString("role_name")
//                        spinner.adapter = ArrayAdapter<String>(this@AddPromoterActivity,
//                            android.R.layout.simple_spinner_dropdown_item,roleName)
//                    }

                    //Toast.makeText(SearchActivity.this,response,Toast.LENGTH_SHORT).show();


//                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(applicationContext, "$status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun populateRoles() {
        var roleNamesArray = ArrayList<String>()
        for (i in 0 until roleListArray.length()) {
            val tempRole = roleListArray.getJSONObject(i)
            roleNamesArray.add(tempRole.getString("role_name"))
        }
        spinner_roleList.adapter = ArrayAdapter<String>(this@AddPromoterActivity,
                android.R.layout.simple_spinner_dropdown_item, roleNamesArray)


    }
    fun getStateList() {
        val call:Call<ResponseBody> = ApiClient.getClient.getStateList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                val jsonObject = JSONObject(res)
                val status = jsonObject.getInt("status")
                var msg: String? = null
                var imageStatus: String? = null
                if (status == 1) {
                    stateList = jsonObject.getJSONArray("state_list")
                    showStates()
                } else {
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
        sp_state.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statesArrayList)
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
        sp_district.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statesArrayList)

    }
}