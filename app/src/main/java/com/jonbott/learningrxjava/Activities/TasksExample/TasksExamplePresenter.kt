package com.jonbott.learningrxjava.Activities.TasksExample

import com.jonbott.learningrxjava.ModelLayer.Entities.Person
import com.jonbott.learningrxjava.ModelLayer.ModelLayer
import io.reactivex.Observable

class TasksExamplePresenter {

    private val modelLayer = ModelLayer.shared
    private val people = listOf(Person("Norris",  "Najar",     0),
                                Person("Dylan",   "Decarlo",   1),
                                Person("Sonny",   "Stecher",   2),
                                Person("Napoleon","Nicols",    3),
                                Person("Jinny",   "Jordahl",   4),
                                Person("Wendi",   "Woodhouse", 5))


    fun loadPeopleInfo(): Observable<List<String>> {
        return modelLayer.loadInfoFor(people)
    }
}