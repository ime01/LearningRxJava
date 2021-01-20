package com.jonbott.learningrxjava.Activities.NetworkExample.Recycler

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.R

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val context = itemView.context
    val cardView: CardView
    val messageIdTextView: TextView
    val userIdTextView: TextView
    val titleTextView: TextView
    val bodyTextView: TextView

    init {
        cardView = itemView.findViewById(R.id.messageCardView)
        messageIdTextView = itemView.findViewById(R.id.messageIdTextView)
        userIdTextView = itemView.findViewById(R.id.userIdTextView)
        titleTextView = itemView.findViewById(R.id.titleTextView)
        bodyTextView = itemView.findViewById(R.id.bodyTextView)
    }

    fun configureWith(message: Message) {
        messageIdTextView.text = message.id.toString()
        userIdTextView.text = message.userId.toString()
        titleTextView.text = message.title
        bodyTextView.text = message.body
    }
}