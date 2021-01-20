package com.jonbott.learningrxjava

import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.Gson
import com.jonbott.learningrxjava.Common.fromJson
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.LocalDatabase
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PersistenceLayer
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import com.jonbott.learningrxjava.SimpleExamples.SimpleRx
import kotlinx.coroutines.experimental.launch

class LearningRxJavaApplication: Application() {

    companion object {
        lateinit var database: LocalDatabase
    }

    override fun onCreate() {
        super.onCreate()

        println("Simple App being used.")

        setupDatabase()

//        SimpleRx.simpleValues()
//        SimpleRx.subjects()
        SimpleRx.basicObservable()
    }

    //region Database Setup Methods

    fun setupDatabase(){
        LearningRxJavaApplication.database = Room.databaseBuilder(this, LocalDatabase::class.java, "LearningRxJavaLocalDatabase").build()

        launch {
            val photoDescriptions = loadJson()
            PersistenceLayer.shared.prepareDb(photoDescriptions)
        }
    }

    fun loadJson(): List<PhotoDescription> {
        val json = loadDescriptionsFromFile()
        val photoDescriptions = Gson().fromJson<List<PhotoDescription>>(json)
        return photoDescriptions
    }

    private fun loadDescriptionsFromFile(): String {
        //ignoring IOExceptions
        val inputStream = assets.open("PhotoDescription.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        return json
    }

    //endregion
}

