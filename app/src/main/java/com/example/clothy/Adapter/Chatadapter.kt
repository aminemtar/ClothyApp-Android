package com.example.clothy.Adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothy.Model.MatchItem
import com.example.clothy.Model.MyApplication
import com.example.clothy.R
import com.example.clothy.ui.ChatActivity


class MessageAdapter(private val messageList: List<MatchItem>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_message_item, parent, false)
        return MessageViewHolder(view)
    }
    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messageList[position]
        val context = holder.itemView.context
        holder.contentTextView.text = item.content
        holder.nameTextView.text = item.name
        holder.chat.setOnClickListener {
            val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = shared.edit()
            editor.putString("reciver", item.name)
            editor.putString("idreciver", item.idR)
            editor.putString("matchID",item.id)
            editor.putString("imageReciver",item.picture)
            editor.apply()

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("username", item.name+" "+item.content)
            intent.putExtra("matcher", item.idR)
            context.startActivity(intent)



        }

        if (item.count <= 0) {
            holder.viewIndicator.visibility = View.INVISIBLE
        }

            Glide.with(context)
                .load(item.picture)
                .into(holder.thumbnail)

        // You can bind other views here
    }




    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        val contentTextView: TextView = itemView.findViewById(R.id.text_content)
       val  thumbnail : ImageView= itemView.findViewById(R.id.thumbnail)
        val viewIndicator:RelativeLayout =itemView.findViewById(R.id.layout_dot_indicator)
        val chat : RelativeLayout = itemView.findViewById(R.id.layout_message_content)
        // You can find other views here
    }


}
