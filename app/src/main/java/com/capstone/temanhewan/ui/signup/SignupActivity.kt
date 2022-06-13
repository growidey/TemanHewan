package com.capstone.temanhewan.ui.signup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.capstone.temanhewan.databinding.ActivitySignupBinding
import com.capstone.temanhewan.ui.login.LoginActivity
import com.capstone.temanhewan.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener {
            val name = binding.etSignupName.text.toString().trim()
            val email = binding.etSignupEmail.text.toString().trim()
            val password = binding.etSignupPass.text.toString().trim()

            if (email.isEmpty()) {
                binding.etSignupEmail.error = "email harus diisi"
                binding.etSignupEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etSignupEmail.error = "email tidak valid"
                binding.etSignupEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etSignupPass.error = "password harus diisi"
                binding.etSignupPass.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etSignupPass.error = "password minimal 6 karakter"
                binding.etSignupPass.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                binding.etSignupName.error = "Full Name required"
                binding.etSignupName.requestFocus()
                return@setOnClickListener

            }

            signupFirebase(name, email, password)
        }
    }

    private fun signupFirebase(
        name: String,
        email: String,
        password: String
    ) {
        val progressDialog = ProgressDialog(this@SignupActivity)
        progressDialog.setMessage("Loading..")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    saveUser(name, email, password, progressDialog)
                } else {
                    val message = it.exception!!.toString()
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUser(
        name: String,
        email: String,
        password: String,
        progressDialog: ProgressDialog
    ) {
        val currentUserId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String, Any>()
        userMap["id"] = currentUserId
        userMap["name"] = name
        userMap["email"] = email
        userMap["password"] = password

        database.child(currentUserId).setValue(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp is Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                val message = it.exception!!.toString()
                Toast.makeText(this, "Error : $message", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
    }
}