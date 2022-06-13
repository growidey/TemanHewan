package com.capstone.temanhewan.ui.profile

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.capstone.temanhewan.R
import com.capstone.temanhewan.databinding.ActivityEditProfileBinding
import com.capstone.temanhewan.utils.ConsVal.Companion.FIREBASE_EMAIL
import com.capstone.temanhewan.utils.ConsVal.Companion.FIREBASE_NAME
import com.capstone.temanhewan.utils.ConsVal.Companion.FIREBASE_NOMOR
import com.capstone.temanhewan.utils.ConsVal.Companion.FIREBASE_PHOTO
import com.capstone.temanhewan.utils.Utils.loadImageURI
import com.capstone.temanhewan.utils.Utils.loadImageUrl
import com.capstone.temanhewan.utils.Utils.setStateColor
import com.capstone.temanhewan.utils.Utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: StorageReference
    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.tittle_edit_profile)
        auth = FirebaseAuth.getInstance()

        retrieveData()
        chooseAva()
    }

    private val result: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.apply {
                    ivProfile.loadImageURI(it)
                    imgUri = it
                }
            }
        }

    private fun uploadAva(uri: Uri?) {
        val emailUser = auth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        storage = FirebaseStorage.getInstance().getReference("User/$setEmail/Photo")

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val date = Date()
        val filename = formatter.format(date)
        val fileRef: StorageReference = storage.child(filename)

        if (uri != null) {
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    val hashMap = HashMap<String, Any>()
                    hashMap[FIREBASE_PHOTO] = it.toString()
                    databaseReference.updateChildren(hashMap)
                    showProgressBar(false)
                    showToast(getString(R.string.save_success), this@EditProfileActivity)
                }
            }.addOnProgressListener {
                showProgressBar(true)
            }.addOnFailureListener {
                showProgressBar(false)
                showToast(getString(R.string.save_failed), this@EditProfileActivity)
            }

        }
    }

    private fun retrieveData() {
        val emailUser = auth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.keepSynced(true)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val photo = snapshot.child(FIREBASE_PHOTO).value
                    val name = snapshot.child(FIREBASE_NAME).value
                    val nomor = snapshot.child(FIREBASE_NOMOR).value
                    val email = snapshot.child(FIREBASE_EMAIL).value
                    binding.apply {
                        edtEmail.setText(email.toString())
                        edtName.setText(name.toString())
                        edtNomor.setText(nomor.toString())
                        if (photo != null) {
                            ivProfile.loadImageUrl(photo.toString())
                        } else {
                            ivProfile.setImageResource(R.drawable.account_circle)
                        }
                        checkInput(name.toString(), nomor.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun checkInput(name: String, nomor: String?) {
        binding.apply {
            val et = listOf(edtName, edtNomor)
            for (editText in et) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val editTextName = edtName.text.toString().trim()
                        val editTextNomor = edtNomor.text.toString().trim()

                        if ((editTextName.isNotEmpty() && editTextName != name) ||
                            (editTextNomor.isNotEmpty() && editTextNomor != nomor)
                        ) {

                            btnSave.setStateColor(
                                this@EditProfileActivity,
                                R.color.blue, true
                            )
                        } else {
                            btnSave.setStateColor(
                                this@EditProfileActivity, R.color.grey, true
                            )
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
        binding.edtEmail.isEnabled = false
        saveUser(name, nomor)
        binding.btnSave.setStateColor(this, R.color.grey, false)
    }

    private fun saveUser(
        name: String?, nomor: String?
    ) {
        binding.apply {
            btnSave.setOnClickListener {
                val textName = edtName.toString().trim()
                val textNomor = edtNomor.toString().trim()
                if (imgUri != null) {
                    if ((textName != name) ||
                        (textNomor != nomor)
                    ) {
                        val hashMap = HashMap<String, Any>()
                        hashMap[FIREBASE_NAME] = textName
                        hashMap[FIREBASE_NOMOR] = textNomor

                        databaseReference.updateChildren(hashMap)
                        uploadAva(imgUri)

                        imgUri = null
                        edtName.clearFocus()
                        edtNomor.clearFocus()
                    } else {
                        uploadAva(imgUri)
                        imgUri = null
                    }
                } else {
                    val hashMap = HashMap<String, Any>()
                    hashMap[FIREBASE_NAME] = textName
                    hashMap[FIREBASE_NAME] = textNomor

                    showProgressBar(true)
                    databaseReference.updateChildren(hashMap).addOnSuccessListener {
                        showProgressBar(false)

                        imgUri != null
                        edtName.clearFocus()
                        edtNomor.clearFocus()
                        showToast(getString(R.string.save_success), this@EditProfileActivity)
                    }.addOnFailureListener {
                        showProgressBar(false)
                        showToast(getString(R.string.save_failed), this@EditProfileActivity)
                    }
                    }

                }
            }
        }
    private fun chooseAva() {
        binding.apply {
            ivProfile.setOnClickListener {
                result.launch("image/*")
            }
            tvChangeProfile.setOnClickListener {
                result.launch("image/*")
            }
        }
    }

        private fun showProgressBar(state: Boolean) {
            binding.progressbar.visibility = if (state) View.VISIBLE else View.GONE

        }
    companion object {
        const val TAG = "EditProfileActivity"
    }
    }
