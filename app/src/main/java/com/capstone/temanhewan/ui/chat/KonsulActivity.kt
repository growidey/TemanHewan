package com.capstone.temanhewan.ui.chat

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.temanhewan.R
import com.capstone.temanhewan.adapter.ListDokterAdapter
import com.capstone.temanhewan.data.Dokter
import com.capstone.temanhewan.databinding.ActivityKonsulBinding
import com.capstone.temanhewan.databinding.ActivityLoginBinding
import com.capstone.temanhewan.ui.PayActivity
import java.util.*

class KonsulActivity : AppCompatActivity() {
    private lateinit var binding : ActivityKonsulBinding
    private lateinit var rvDokter: RecyclerView
    private val list = ArrayList<Dokter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityKonsulBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvDokter = findViewById(R.id.rv_dokter)
        rvDokter.setHasFixedSize(true)

        list.addAll(listDokter)
        showRecyclerList()
    }

    private val listDokter: ArrayList<Dokter>
        get() {
            val dataName = resources.getStringArray(R.array.data_name)
            val dataPrice = resources.getStringArray(R.array.data_price)
            val dataAva = resources.obtainTypedArray(R.array.data_avatar)
            val listDok = ArrayList<Dokter>()
            for (i in dataName.indices) {
                val dokter = Dokter(dataName[i], dataPrice[i], dataAva.getResourceId(i, -1))
                listDok.add(dokter)
            }
            return listDok
        }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvDokter.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvDokter.layoutManager = LinearLayoutManager(this)
        }
        val listDokterAdapter = ListDokterAdapter(list)
        rvDokter.adapter = listDokterAdapter
        listDokterAdapter.setOnItemClickCallback(object : ListDokterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Dokter) {
                val payKonsul = Intent(this@KonsulActivity, PayActivity::class.java)
                payKonsul.putExtra(PayActivity.EXTRA_USER, data)
                startActivity(payKonsul)
                showSelectedDokter(data)
            }
        })
    }

    private fun showSelectedDokter(dokter: Dokter) {
        Toast.makeText(this, "Kamu memilih " + dokter.name, Toast.LENGTH_SHORT).show()

    }
}