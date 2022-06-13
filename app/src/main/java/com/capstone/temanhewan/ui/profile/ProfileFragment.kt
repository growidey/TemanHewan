package com.capstone.temanhewan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.temanhewan.R
import com.capstone.temanhewan.data.User
import com.capstone.temanhewan.databinding.FragmentHomeBinding
import com.capstone.temanhewan.databinding.FragmentProfileBinding
import com.capstone.temanhewan.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private var mbinding: FragmentProfileBinding? = null
    private val binding get() = mbinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var userFirebase: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mbinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userFirebase = FirebaseAuth.getInstance().currentUser!!

        initUser()
        logout()
    }


    private fun initUser() {
        val userRef =
            FirebaseDatabase.getInstance().getReference().child("Users").child(userFirebase.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    binding.apply {
                        tvProfileName.text = user?.name
                        tvProfileEmail.text = userFirebase.email
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun logout() {
        binding.buttonLogout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(requireContext(), getString(R.string.logout_auth), Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}