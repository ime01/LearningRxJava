package com.jonbott.learningrxjava.Activities.DatabaseExample.Recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import com.jonbott.learningrxjava.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

typealias ItemClickedlambda = (v: View, position: Int) -> Unit

class PhotoDescriptionViewAdapter(var onItemClicked: ItemClickedlambda): RecyclerView.Adapter<PhotoDescriptionViewHolder>() {

    internal var photoDescriptions = BehaviorRelay.createDefault(mutableListOf<PhotoDescription>())
    private val bag = CompositeDisposable()

    init {
        photoDescriptions.observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    notifyDataSetChanged()
                }.disposedBy(bag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_description, parent, false)
        val viewHolder = PhotoDescriptionViewHolder(view)

        view.setOnClickListener { v -> onItemClicked(v, viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PhotoDescriptionViewHolder, position: Int) {
        val photoDescription = photoDescriptions.value[position]
        holder.configureWith(photoDescription)
    }

    override fun getItemCount(): Int = photoDescriptions.value.size

}