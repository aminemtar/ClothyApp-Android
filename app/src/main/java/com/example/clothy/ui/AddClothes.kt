package com.example.clothy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.MainActivity
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.opencv.android.InstallCallbackInterface
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class AddClothes : AppCompatActivity(), LoaderCallbackInterface{
    private lateinit var myImage: ImageView
    private lateinit var  typee: TextView
    private lateinit var  Color: ImageView
    private lateinit var  taille: TextView
    private lateinit var  Catg: EditText
    private lateinit var retake:ImageView
    private lateinit var XS : Button
    private lateinit var pointure : NumberPicker
    private lateinit var S : Button
    private lateinit var M : Button
    private lateinit var L : Button
    private lateinit var XL : Button
    private lateinit var size : String
    private lateinit var category:RequestBody
    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_clothes)
        XS = findViewById(R.id.btSizeXS)
        S = findViewById(R.id.btSizeS)
        M =findViewById(R.id.btSizeM)
        L= findViewById(R.id.btSizeL)
        XL = findViewById(R.id.btSizeXL)
        pointure=findViewById(R.id.pointure)
        myImage = findViewById(R.id.myImage)
        retake =findViewById(R.id.retake)
        retake.setOnClickListener {
            startActivity(Intent(this,OpenCamera::class.java))
        }

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, this)
        } else {
            onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }

    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onManagerConnected(status: Int) {
        when (status) {
            LoaderCallbackInterface.SUCCESS -> {
                // Load the cascade classifier for face detection
                val `is` = resources.openRawResource(R.raw.lbpcascade_frontalface)
                val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
                mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
                val os = FileOutputStream(mCascadeFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (`is`.read(buffer).also { bytesRead = it } != -1) {
                    os.write(buffer, 0, bytesRead)
                }
                `is`.close()
                os.close()
                mJavaDetector = CascadeClassifier(mCascadeFile!!.absolutePath)
                val extras = intent.extras
                val imageBitmap = extras?.getParcelable("image") as? Bitmap

                // Convert the bitmap to a Mat
                val image = Mat()
                Utils.bitmapToMat(imageBitmap, image)
                // Convert the image to grayscale
                Imgproc.cvtColor(image, image, Imgproc.CHAIN_APPROX_NONE)

                // Detect faces in the image
                val faces = MatOfRect()
                mJavaDetector!!.detectMultiScale(image, faces)

                // Blur each detected face
                for (face in faces.toArray()) {
                    Log.e("faces", face.toString())
                    // Extract the region of the image containing the face
                    val faceMat = Mat(image, Rect(face.x, face.y, face.width, face.height))
                    // Apply a Gaussian blur to the face
                    Imgproc.GaussianBlur(faceMat, faceMat, Size(109.0, 109.0), 90.0)
                    // Put the blurred face back into the image
                    faceMat.copyTo(image.submat(face))
                }
                 Log.e("faces",faces.toString())
                // Convert the image back to a bitmap
                Utils.matToBitmap(image, imageBitmap)
                os.close()
                image.release()
                faces.release()
                // Display the blurred image in an ImageView
                val centerX = imageBitmap!!.width / 2
                val centerY = imageBitmap.height / 2
                val color = imageBitmap.getPixel(centerX, centerY)
                myImage.setImageBitmap(imageBitmap)
                val localModel = LocalModel.Builder()
                    .setAssetFilePath("model (3).tflite")
                    .build()
                val txtOutput : TextView = findViewById(R.id.txtRewardDetailName)

                val options = CustomImageLabelerOptions.Builder(localModel)
                    .setConfidenceThreshold(0.2f)
                    .setMaxResultCount(1)
                    .build()
                val labeler = ImageLabeling.getClient(options)
                val imagee = imageBitmap.let { InputImage.fromBitmap(it,0) }
                var outputText = ""
                var typ = ""
                labeler.process(imagee)
                    .addOnSuccessListener { labels ->
                        // Task completed successfully
                        for (label in labels) {
                            val text = label.text
                            val confidence = label.confidence
                            Log.e("confidence",confidence.toString())
                            outputText += "$text : $confidence\n"
                            typ += text
                            //val index = label.index
                            txtOutput.text = text
                        }



                     //   val drawable = myImage.drawable

        // Get the color of the drawable
                        /* val color = when (drawable) {
                                     is ColorDrawable -> drawable.color
                                     is BitmapDrawable -> getDominantColor(drawable.bitmap)
                                     else -> throw IllegalArgumentException("Unsupported drawable type")
                                 }

                                 */
                        //txtOutput.setTextColor(color)
                        println(color)
                        val hexColor = java.lang.String.format("#%06X", 0xFFFFFF and color)
                        Color = findViewById(R.id.txtRewardDetailPoint)
                        //Color.setBackgroundColor(hexColor)
                        Color.setBackgroundColor(color)
                        typee =findViewById(R.id.txtRewardDetailName)
                        taille = findViewById(R.id.textView11)
                        Catg = findViewById(R.id.txtDescription)
                        typee.text = typ
                        if (typ == "shoes"){
                            XS.visibility = View.GONE
                            S.visibility = View.GONE
                            M.visibility = View.GONE
                            L.visibility = View.GONE
                            XL.visibility = View.GONE
                            pointure.visibility = View.VISIBLE
                            pointure.maxValue =20
                            pointure.maxValue=55
                            pointure.isHorizontalScrollBarEnabled
                            pointure.setOnValueChangedListener { picker, oldVal, newVal ->
                                size = newVal.toString()
                            }



                        }else{
                            XS.setOnClickListener {
                                size= XS.text.toString()
                                it.setBackgroundColor(android.graphics.Color.rgb(225, 155, 155))
                                L.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                M.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                S.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                XL.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                            }

                            S.setOnClickListener {
                                size= S.text.toString()
                                it.setBackgroundColor(android.graphics.Color.rgb(225, 155, 155))
                                XS.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                L.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                M.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                XL.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                            }

                            M.setOnClickListener {
                                size= M.text.toString()
                                it.setBackgroundColor(android.graphics.Color.rgb(225, 155, 155))
                                XS.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                S.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                L.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                XL.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                            }
                            M.performClick()

                            L.setOnClickListener {
                                size= L.text.toString()
                                it.setBackgroundColor(android.graphics.Color.rgb(225, 155, 155))
                                XS.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                S.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                M.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                XL.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                            }

                            XL.setOnClickListener {
                                size= XL.text.toString()
                                it.setBackgroundColor(android.graphics.Color.rgb(225, 155, 155))
                                XS.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                S.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                M.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                                L.setBackgroundColor(android.graphics.Color.rgb(250,250,250))
                            }
                        }
                        val cc: Button = findViewById(R.id.btnRewardDetialClaim)

                        cc.setOnClickListener {
                            val bitmap = (myImage.drawable as BitmapDrawable).bitmap
                            val file = File(cacheDir, "image.jpg")
                            val outputStream = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.flush()
                            outputStream.close()
                            val mediaType = "image/jpeg".toMediaTypeOrNull()
                            val requestFile = file.asRequestBody(mediaType)
                            val imagePart = MultipartBody.Part.createFormData(
                                "photo",
                                file.name,
                                requestFile
                            )
                            val typee = typ.toRequestBody("text/plain".toMediaTypeOrNull())
                            val taille = size.toRequestBody("text/plain".toMediaTypeOrNull())
                            val couleur = hexColor.toRequestBody("text/plain".toMediaTypeOrNull())
                            if (Catg.text.isEmpty()){
                               category="No description".toRequestBody("text/plain".toMediaTypeOrNull())
                            }else{
                                category = Catg.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                            }

                            val retro =
                                RetrofitClient().getInstance().create(OutfitService::class.java)

                            val call =
                                retro.uploadImage(imagePart, typee, taille, couleur, category)
                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    // Handle the error
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)

                                    finish()
                                }
                            })
                        }
                    }
                    .addOnFailureListener { e ->

                    }
            }

        }
    }

    override fun onPackageInstall(operation: Int, callback: InstallCallbackInterface?) {
        TODO("Not yet implemented")
    }

    fun goBack(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

}