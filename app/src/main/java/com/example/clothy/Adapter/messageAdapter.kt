package com.example.clothy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.clothy.Model.Message
import com.example.clothy.Model.MyApplication
import com.example.clothy.R
import org.json.JSONObject

data class ChatMessage(val sender: String, val message: String, val isSender: Boolean)
data class MSG(val _id: String, val message: String, var to: String, var from: String, val matchID :String, val createdAt: String, val updatedAt:String,
               var isSender: Boolean)

class msgAdapter(context: Context, messages: MutableList<JSONObject>) :
    ArrayAdapter<JSONObject>(context, 0, messages) {

    companion object {
        private const val VIEW_TYPE_SENDER = 0
        private const val VIEW_TYPE_RECIPIENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val reciver = shared.getString("reciver","").toString()
       val idreciver = shared.getString("idreciver","").toString()
        val id = shared.getString("id","").toString()
        val message = getItem(position)
        println(message.toString())
        return if (message!!.has("isSender")) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECIPIENT
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)
        val viewType = getItemViewType(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            if (viewType == VIEW_TYPE_SENDER) {
                R.layout.messagesrow
            } else {
                R.layout.messagerowreciver
            }, parent, false
        )

        val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
        val senderTextView = view.findViewById<TextView>(R.id.senderTextView)


        senderTextView.text = message!!.has("to").toString()
        messageTextView.text = message.has("message").toString()

        return view
    }

}