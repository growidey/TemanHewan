package com.capstone.temanhewan.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.temanhewan.databinding.FragmentHomeBinding
import com.capstone.temanhewan.ui.chat.ChatbotActivity
import com.capstone.temanhewan.ui.chat.KonsulActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private var mbinding: FragmentHomeBinding? = null
    private val binding get() = mbinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mbinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        mbinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        binding.btnKonsul.setOnClickListener {
            val k = Intent(requireContext(), KonsulActivity::class.java)
            startActivity(k)
        }
        binding.btnChatBot.setOnClickListener {
            val b = Intent(requireContext(), ChatbotActivity::class.java)
            startActivity(b)
        }
        val emailUser = auth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.keepSynced(true)
    }
}

