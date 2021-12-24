package com.example.testing_kotlin_samples.tinder

import android.text.Layout
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


import androidx.recyclerview.widget.RecyclerView
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.bookreview.model.Historybook


class CardItemAdapter: ListAdapter<Carditem, CardItemAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun bind(carditem: Carditem){
            view.findViewById<TextView>(R.id.nameTV).text = carditem.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Carditem>(){
            override fun areItemsTheSame(oldItem: Carditem, newItem: Carditem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Carditem, newItem: Carditem): Boolean {
                return oldItem.userId == newItem.userId
            }

        }
    }

}
