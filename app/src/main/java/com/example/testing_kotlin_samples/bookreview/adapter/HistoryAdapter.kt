package com.example.testing_kotlin_samples.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testing_kotlin_samples.bookreview.model.Book
import com.example.testing_kotlin_samples.bookreview.model.Historybook
import com.example.testing_kotlin_samples.databinding.ItemBookBinding
import com.example.testing_kotlin_samples.databinding.ItemHistoryBinding

class HistoryAdapter(val historyDeleteClickedListener : (String)-> Unit):ListAdapter<Historybook,HistoryAdapter.HistoryItemViewHolder>(diffUtil) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
//        holder.bind(getItem(position))
    }




    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(history: Historybook){
            binding.historykeywordTextView.text = history.keyword
            binding.historykeyworddeletebutton.setOnClickListener {
                historyDeleteClickedListener(history.keyword.orEmpty())
            }
        }

    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Historybook>(){
            override fun areItemsTheSame(oldItem: Historybook, newItem: Historybook): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Historybook, newItem: Historybook): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}