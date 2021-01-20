package com.jonbott.learningrxjava.Activities.DatabaseExample

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonbott.learningrxjava.ModelLayer.ModelLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class DatabaseExamplePresenter {
    val modelLayer = ModelLayer.shared //normally injected

    val photoDescription : BehaviorRelay<List<PhotoDescription>>
        get()= modelLayer.photoDescriptions
//    bubbling up from the lower layer
    init {
        launch {
            delay(3000)
            modelLayer.loadAllPhotoDescriptions()
        }
    }


}