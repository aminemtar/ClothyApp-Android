package com.example.clothy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.services.EchoWebSocketListener
import com.clothy.clothyandroid.services.EchoWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.cookies
import com.example.clothy.Adapter.LockedAdapter
import com.example.clothy.Adapter.MSG
import com.example.clothy.Adapter.OutfitAdapter
import com.example.clothy.Adapter.msgAdapter
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatActivity : AppCompatActivity() {
    private val message : ImageView by lazy { findViewById(R.id.likeIconID) }
    private val output: TextView by lazy { findViewById(R.id.messageTextView) }
    private val entryText: EditText by lazy { findViewById(R.id.messageEdittextID) }
    private val client by lazy {
        OkHttpClient()
    }
    private lateinit var adapterr: msgAdapter
    private lateinit var adapter: OutfitAdapter
    private lateinit var recyclerView: RecyclerView
    private var Rcieved =  mutableListOf<MSG>()
    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var button: ImageView
    private lateinit var send: Button
    private lateinit var username :TextView
    private lateinit var userImage : ImageView
    private lateinit var id :String
    private lateinit var sender :String
    private lateinit var idR :String
    private lateinit var imaageR :String
    private lateinit var reciver :String
    private lateinit var matchID :String
    private lateinit var idreciver :String
    private lateinit var seelocked: ImageView
    var outfits = ArrayList<OutfitResponse.Outfit>()



    private var ws: WebSocket? = null
    private var isListVisible = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        reciver = shared.getString("reciver","").toString()
        idreciver = shared.getString("idreciver","").toString()
        id = shared.getString("id","").toString()
        sender = shared.getString("firstname","").toString()
        idR = shared.getString("idreciver","").toString()
        imaageR = shared.getString("imageReciver","").toString()
        matchID = shared.getString("matchID","").toString()
        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.messageEdittextID)
        button = findViewById(R.id.likeIconID)
        username= findViewById(R.id.user_name)
        userImage= findViewById(R.id.user_image)
        seelocked = findViewById(R.id.locked)
       // val myListComposeView = findViewById<ComposeView>(R.id.myList)
        val toggleButton = findViewById<ImageView>(R.id.cameraIconID)

        toggleButton.setOnClickListener {
            if(isListVisible)
            {
                recyclerView.visibility = View.GONE
            }else {
                recyclerView.visibility = View.VISIBLE
            }

            isListVisible = !isListVisible


        }



        seelocked.setOnClickListener {
            val intent = Intent(this,LockedActivity::class.java)
            startActivity(intent)
        }


// Set the adapter for the recyclerView

recyclerView=findViewById(R.id.horizontalList)
        adapterr = msgAdapter(this, Rcieved)
        listView.adapter = adapterr

        listView.setSelection(listView.count - 1);
        adapterr.notifyDataSetChanged()
        Log.e("messages",Rcieved.size.toString())
        button.setOnClickListener {
            val message = editText.text.toString()
            Rcieved += MSG("Clark Kent", message, "true","","","","",true)
            adapterr.notifyDataSetChanged()
            editText.text.clear()

        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                    button.visibility = View.VISIBLE
                } else {
                    button.visibility = View.GONE
                }
            }
        })
        message.setOnClickListener {
            ws?.apply {
                val text = entryText.text.toString()
                Rcieved.add(MSG("",text,"",sender!!,matchID,"","",true))

                editText.text.clear()
                val message = JSONObject()
                message.put("to", idreciver)
                message.put("idMatch", matchID)
                message.put("message", text)
                send(message.toString())
                listView.setSelection(adapterr.count - 1)
                adapterr.notifyDataSetChanged()
            } ?: ping("Error: Restart the App to reconnect")
        }
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getSwiped().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.get(0)?.type?.let { Log.e("type", it) }
                    if (user != null) {
                        val layoutManager =
                            LinearLayoutManager(MyApplication.getInstance(), LinearLayoutManager.HORIZONTAL, false)
                        recyclerView.layoutManager = layoutManager
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
         adapter = OutfitAdapter(outfits)
        recyclerView.adapter = adapter
    }
    private fun setupRecyclerViewAdapter() {
        recyclerView = findViewById(R.id.horizontalList)
        adapter = OutfitAdapter(outfits)
        recyclerView.adapter = adapter
        adapter.submitList(outfits)

    }


    override fun onResume() {
        super.onResume()
        start()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    private fun start() {

        val request: Request = Request.Builder().url("ws://10.0.2.2:9090/room/$matchID").build()
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        val client = OkHttpClient.Builder()
            .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
            .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
            .build()
        ws = client.newWebSocket(request, listener)
        Rcieved.clear()
        val key1 = intent.getStringExtra("username")

        username.text= key1.toString()
        Log.e("username",key1.toString())
        Glide.with(applicationContext)
            .load(imaageR)
            .into(userImage)
        listView.setSelection(listView.count - 1);
        button.visibility= View.GONE
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        adapterr.notifyDataSetChanged()

    }

    private fun stop() {
        ws?.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    private fun output(txt: String) {
        runOnUiThread {

            val jsonString = """[$txt]"""

            val jsonArray = JSONArray(jsonString)
            val messageList = mutableListOf<MSG>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val messageItem = Gson().fromJson(jsonObject.getString("msg"), MSG::class.java)
                if (messageItem.from == id)
                {
                    messageItem.isSender= true
                    messageItem.from= sender

                }
                if(messageItem.from == idR){
                    messageItem.isSender =false
                    messageItem.from= reciver
                }
                Rcieved.add(messageItem)
                Log.e("message",Rcieved[0].message)
                val key1 = intent.getStringExtra("username")

                username.text= key1.toString()
                Glide.with(applicationContext)
                    .load(imaageR)
                    .into(userImage)
                listView.setSelection(listView.count - 1);
                adapterr.notifyDataSetChanged()
            }

            //"${output.text}\n${txt}".also { output.text = it }
        }
    }

    private fun ping(txt: String) {
        runOnUiThread {
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
        }
    }
    fun goBack(view: View) {
        finish()
    }
}




