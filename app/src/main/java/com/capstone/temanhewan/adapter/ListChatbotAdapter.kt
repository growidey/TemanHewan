package com.capstone.temanhewan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.temanhewan.R
import com.capstone.temanhewan.data.Chatbot
import com.capstone.temanhewan.utils.ConsVal.Companion.RECEIVE_ID
import com.capstone.temanhewan.utils.ConsVal.Companion.SEND_ID

class ListChatbotAdapter : RecyclerView.Adapter<ListChatbotAdapter.MessageViewHolder>() {

    private var botList = mutableListOf<Chatbot>()

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        val tvBot: TextView = itemView.findViewById(R.id.tv_bot_message)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return botList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = botList[position]
        when (currentMessage.id) {
            SEND_ID -> {
                holder.tvMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tvBot.visibility = View.GONE
            }
            RECEIVE_ID -> {
                holder.tvBot.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.tvMessage.visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: Chatbot) {
        this.botList.add(message)
        notifyItemInserted(botList.size)
    }

}