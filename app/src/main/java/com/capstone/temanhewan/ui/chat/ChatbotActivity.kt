package com.capstone.temanhewan.ui.chat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.temanhewan.R
import com.capstone.temanhewan.adapter.ListChatbotAdapter
import com.capstone.temanhewan.data.Chatbot
import com.capstone.temanhewan.databinding.ActivityChatbotBinding
import com.capstone.temanhewan.network.BotResponse
import com.capstone.temanhewan.utils.ConsVal.Companion.OPEN_GOOGLE
import com.capstone.temanhewan.utils.ConsVal.Companion.OPEN_SEARCH
import com.capstone.temanhewan.utils.ConsVal.Companion.RECEIVE_ID
import com.capstone.temanhewan.utils.ConsVal.Companion.SEND_ID
import com.capstone.temanhewan.utils.Time
import kotlinx.coroutines.*

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding
    private var botList = mutableListOf<Chatbot>()
    private lateinit var adapter: ListChatbotAdapter
    private val botName = listOf("Dey", "Nunu", "Sasa", "Juju", "Fal", "Ghov")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.tittle_chatbot)
        greeting()
        showRecyclerView()
        clickEvents()
    }

    private fun showRecyclerView() {
        adapter = ListChatbotAdapter()
        binding.messageRecyclerView.adapter = adapter
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

    }

    private fun greeting() {
        val random = (0..3).random()
        customChatBot("Hello! Aku ${botName[random]}, Gimana kondisi peliharanmu?")
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun customChatBot(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                botList.add(Chatbot(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Chatbot(message, RECEIVE_ID, timeStamp))

                binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                botList.add(Chatbot(response, RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(Chatbot(response, RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etChat.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            botList.add(Chatbot(message, SEND_ID, timeStamp))
            binding.etChat.setText("")

            adapter.insertMessage(Chatbot(message, SEND_ID, timeStamp))
            binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun clickEvents() {

        //Send a message
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        binding.etChat.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }
}