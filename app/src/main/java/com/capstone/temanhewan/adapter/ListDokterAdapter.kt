package com.capstone.temanhewan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.temanhewan.data.Dokter
import com.capstone.temanhewan.databinding.ItemDokterBinding

class ListDokterAdapter(private val listDokter: ArrayList<Dokter>) : RecyclerView.Adapter<ListDokterAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, price, avatar) = listDokter[position]
        holder.binding.tvNameDoctor.text = name
        holder.binding.tvPriceDoctor.text = price
        holder.binding.ivDoctor.setImageResource(avatar)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listDokter[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = listDokter.size

    class ListViewHolder(var binding : ItemDokterBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: Dokter)
    }
}
