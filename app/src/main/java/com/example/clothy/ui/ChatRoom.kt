package com.example.clothy.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.services.EchoWebSocketListener
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.cookies
import com.example.clothy.Adapter.MessageA
import com.example.clothy.Adapter.OutfitAdapter
import com.example.clothy.Model.Message
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.example.clothy.Service.MatchService
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class ChatRoom : AppCompatActivity() ,TextWatcher{
    private var name: String? = null
    private lateinit var webSocket: WebSocket
    private val SERVER_PATH = "ws://10.0.2.2:9090/room/6460682f0da1239bb9ceb613"
    private lateinit var messageEdit: EditText
    private lateinit var sendBtn: View
    private lateinit var pickImgBtn: View
    private lateinit var recyclerView: RecyclerView
    private val IMAGE_REQUEST_ID = 1
    private lateinit var id :String
    private lateinit var sender :String
    private lateinit var idR :String
    private lateinit var imaageR :String
    private lateinit var username : TextView
    private lateinit var userImage : ImageView
    private lateinit var seelocked: ImageView
    private lateinit var reciver :String
    private lateinit var matchID :String
    private lateinit var idreciver :String
    private lateinit var messageAdapter: MessageA
    private lateinit var recyclerViewO: RecyclerView
    private lateinit var adapter: OutfitAdapter
    private var isListVisible = true
    var outfits = ArrayList<OutfitResponse.Outfit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        reciver = shared.getString("reciver","").toString()
        idreciver = shared.getString("idreciver","").toString()
        id = shared.getString("id","").toString()
        sender = shared.getString("firstname","").toString()
        matchID = shared.getString("matchID","").toString()
        imaageR = shared.getString("imageReciver","").toString()
        username= findViewById(R.id.user_name)
        userImage= findViewById(R.id.user_image)
        seelocked = findViewById(R.id.locked)
        seelocked.setOnClickListener {
            val intent = Intent(this,LockedActivity::class.java)
            startActivity(intent)
        }
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getSwiped().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: retrofit2.Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.get(0)?.type?.let { Log.e("type", it) }
                    if (user != null) {
                        val layoutManager =
                            LinearLayoutManager(MyApplication.getInstance(), LinearLayoutManager.HORIZONTAL, false)
                        recyclerViewO.layoutManager = layoutManager
                        outfits = user as ArrayList<OutfitResponse.Outfit>
                        setupRecyclerViewAdapter()
                        adapter.notifyDataSetChanged()

                    }
                }
                else{
                    Log.e("Error", "error")
                }
            }

            override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
                // Handle failure
            }
        })

        recyclerViewO =findViewById(R.id.horizontalList)
        recyclerViewO.visibility = View.GONE
        adapter = OutfitAdapter(outfits)
        recyclerViewO.adapter = adapter
        getmessages()
       // name = intent.getStringExtra("name")
        initiateSocketConnection()
    }
    private fun initiateSocketConnection() {
        val request: Request = Request.Builder().url("ws://10.0.2.2:9090/room/$matchID").build()
        val client = OkHttpClient.Builder()
            .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
            .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
            .build()
        webSocket = client.newWebSocket(request, SocketListener())
        val key1 = intent.getStringExtra("username")

        username.text= key1.toString()
        Log.e("username",key1.toString())
        Glide.with(applicationContext)
            .load(imaageR)
            .into(userImage)


    }
    fun goBack(view: View) {
        finish()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }


    override fun afterTextChanged(s: Editable?) {
        val string = s.toString().trim()

        if (string.isEmpty()) {
            resetMessageEdit()
        } else {
            sendBtn.visibility = View.VISIBLE
            pickImgBtn.visibility = View.INVISIBLE
        }
    }
    private fun setupRecyclerViewAdapter() {
        recyclerViewO = findViewById(R.id.horizontalList)
        adapter = OutfitAdapter(outfits)
        recyclerViewO.adapter = adapter
        adapter.submitList(outfits)

    }
    private inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            runOnUiThread {
                Toast.makeText(this@ChatRoom, "Socket Connection Successful!", Toast.LENGTH_SHORT).show()
                initializeView()
            }
        }
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        runOnUiThread {
            try {
                println(text)
                val jsonObject = JSONObject(text)
                jsonObject.put("isSent", false)

                messageAdapter.addItem(jsonObject)

                recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}
    private fun initializeView() {
        messageEdit = findViewById(R.id.messageEdit)
        sendBtn = findViewById(R.id.sendBtn)
        pickImgBtn = findViewById(R.id.pickImgBtn)
        recyclerView = findViewById(R.id.recyclerView)

        messageAdapter = MessageA(layoutInflater)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageEdit.addTextChangedListener(this)

        sendBtn.setOnClickListener {
            val message = JSONObject()
            try {
                message.put("to", idreciver)
                message.put("idMatch", matchID)
                println(idreciver.toString())
                println(matchID.toString())
                message.put("message", messageEdit.text.toString())
                webSocket.send(message.toString())
                message.put("isSent", true)
                messageAdapter.addItem(message)

                recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)

                resetMessageEdit()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        pickImgBtn.setOnClickListener {
            if(isListVisible)
            {
                recyclerViewO.visibility = View.GONE
            }else {
                recyclerViewO.visibility = View.VISIBLE
            }

            isListVisible = !isListVisible
        }
    }
    private fun resetMessageEdit() {
        messageEdit.removeTextChangedListener(this)

        messageEdit.setText("")
        sendBtn.visibility = View.INVISIBLE
        pickImgBtn.visibility = View.VISIBLE

        messageEdit.addTextChangedListener(this)
    }
    private fun sendImage(image: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        val jsonObject = JSONObject()

        try {
            jsonObject.put("name", name)
            jsonObject.put("image", base64String)

            webSocket.send(jsonObject.toString())

            jsonObject.put("isSent", true)
            messageAdapter.addItem(jsonObject)

            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {
            try {
                val `is`: InputStream? = contentResolver.openInputStream(data?.data!!)
                val image: Bitmap = BitmapFactory.decodeStream(`is`)

                sendImage(image)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    private fun getmessages() {
        val retro = RetrofitClient().getInstance().create(MatchService::class.java)
        retro.getmessages(matchID).enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: retrofit2.Response<List<Message>>) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    if (messages != null) {
                        for (message in messages) {
                            if (message.from == id) {
                                message.isSender = true
                                message.from = sender
                                val jsonObject = JSONObject()
                                jsonObject.put("message", message.message)
                                jsonObject.put("isSent", true)
                                messageAdapter.addItem(jsonObject)
                            } else if (message.from == idreciver) {
                                message.isSender = false
                                message.from = reciver
                                val jsonObject = JSONObject()
                                jsonObject.put("message", message.message)
                                jsonObject.put("isSent", false)
                                messageAdapter.addItem(jsonObject)
                            }
                        }
                        if(messageAdapter.itemCount>0){
                            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                // Handle failure
                Log.e("Error", "error")
            }
        })
    }


}