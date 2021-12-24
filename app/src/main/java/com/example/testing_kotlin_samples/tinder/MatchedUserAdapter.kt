package com.example.testing_kotlin_samples.tinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_kotlin_samples.R

class MatchedUserAdapter:ListAdapter<Carditem, MatchedUserAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(carditem: Carditem){
            view.findViewById<TextView>(R.id.userNameTextView).text = carditem.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_matched_user,parent,false))
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

