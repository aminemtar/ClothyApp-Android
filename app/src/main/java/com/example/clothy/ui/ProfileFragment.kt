package com.example.clothy.ui
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserResponse
import com.example.clothy.Service.UserService
import com.example.clothy.databinding.FragmentProfileBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var locked:TextView
    private lateinit var terms :TextView
    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1
    }
    private lateinit var imagename:String
    private lateinit var userImageView:ImageView
    private lateinit var contact:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("firstname", "")
        val lastname = sharedPreferences?.getString("lastname", "")
        val fullname = username+" "+lastname
        val usernameTextView = binding.txtUsername
        locked=binding.locked
        contact=binding.contact
        contact.setOnClickListener { startActivity(Intent(requireContext(),ContactUs::class.java)) }
        terms =binding.termsandconditions
        terms.setOnClickListener {
 startActivity(Intent(requireContext(),TermsandConditions::class.java))
        }
        val image = binding.imgUserPic
        val editButton = binding.txtProfile
        val logout =binding.txtLogout
        locked.setOnClickListener { startActivity(Intent(requireContext(),AllLockedActivity::class.java)) }
        val url = RetrofitClient().BASE_URLL+sharedPreferences.getString("image","")
        if(sharedPreferences.getString("image","")!="aa")
        {
            Glide.with(this).load(url).into(image)
        }

        editButton.setOnClickListener{
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }
        logout.setOnClickListener {

            val editor = sharedPreferences?.edit()
            if (editor != null) {
                editor.clear()
                editor.apply()
                RetrofitClient.CookieStorage.cookies.clear()

                Log.e("cookies", RetrofitClient.CookieStorage.cookies.toString())
            }
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
val reset =binding.txtSetting
        reset.setOnClickListener {
            startActivity(Intent(requireContext(),ResetPassword::class.java))
        }

        usernameTextView.text = fullname
         userImageView = binding.imgUserPic

        userImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imageActivityResultLauncher.launch(galleryIntent)
        }


        return binding.root
    }

    private val imageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            // Process the selected image
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    // Load the selected image into the ImageView
                    userImageView.setImageURI(selectedImageUri)
                    val bitmap = (userImageView.drawable as BitmapDrawable).bitmap
                    val file = File(requireContext().cacheDir, "image.jpg")
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    val mediaType = "image/jpeg".toMediaTypeOrNull()
                    val requestFile = file.asRequestBody(mediaType)
                    val imagePart = MultipartBody.Part.createFormData(
                        "imageF",
                        file.name,
                        requestFile
                    )
                    val retro = RetrofitClient().getInstance().create(UserService::class.java)
                    val call = retro.uploadImage(imagePart)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                getUser()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("mataaditch","error")
                        }
                    })

                }
            }
        }
    }
    fun getUser()
    {
        val retro = RetrofitClient().getInstance().create(UserService::class.java)
        retro.user().enqueue(object : Callback<UserResponse.User> {
            override fun onResponse(call: Call<UserResponse.User>, response: Response<UserResponse.User>) {
                if(response.isSuccessful){
                    val user = response.body()
                    imagename= user!!.image.toString()
                    Log.e("image",user.image.toString())
                    val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("image", imagename)
                    editor.apply()
                } else {
                    Log.e("mataaditch","please check email")
                }
            }

            override fun onFailure(call: Call<UserResponse.User>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }
        })
    }
}