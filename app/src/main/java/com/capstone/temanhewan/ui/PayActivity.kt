package com.capstone.temanhewan.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.capstone.temanhewan.R
import com.capstone.temanhewan.data.Dokter
import com.capstone.temanhewan.databinding.ActivityPayBinding

class PayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dokter = intent.getParcelableExtra<Dokter>(EXTRA_USER) as Dokter
        binding.ivUser.setImageResource(dokter.avatar)
        binding.payNameDoktor.text = dokter.name
        binding.payHargaDoktor.text = dokter.price
        binding.btnNext.setOnClickListener{
            val intent = Intent(this@PayActivity, FinishActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_dana ->
                    if (checked) {
                    }

                R.id.radio_shopee ->
                    if (checked) {
                    }
            }
        }
    }
}