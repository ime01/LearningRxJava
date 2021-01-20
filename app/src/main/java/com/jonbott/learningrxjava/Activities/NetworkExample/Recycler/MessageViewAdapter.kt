package com.jonbott.learningrxjava.Activities.NetworkExample.Recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.Activities.DatabaseExample.Recycler.ItemClickedlambda
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MessageViewAdapter(var onItemClicked: ItemClickedlambda): RecyclerView.Adapter<MessageViewHolder>() {

    internal val messages = BehaviorRelay.createDefault(listOf<Message>())

    private val bag = CompositeDisposable()

    init {
        messages.observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    notifyDataSetChanged()
                }.disposedBy(bag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        val viewHolder = MessageViewHolder(view)

        view.setOnClickListener { v -> onItemClicked(v, viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.value[position]
        holder.configureWith(message)
    }

    override fun getItemCount(): Int = messages.value.size

}