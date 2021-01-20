package com.jonbott.learningrxjava.SimpleExamples

import com.jonbott.learningrxjava.Common.disposedBy
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

object TraitsRx {
    var bag = CompositeDisposable()

    fun traits_single() {
        val single = Single.create<String> { single ->
            //do some logic here
            val success = true


            if (success) { //return a value
                single.onSuccess("nice work!")
            } else {
                val someException = IllegalArgumentException("some fake error")
                single.onError(someException)
            }
        }

        single.subscribe({ result ->
                //do something with result
            println("👻 single: ${ result }")
        }, { error ->
            //do something for error
        }).disposedBy(bag)
    }


    fun traits_completable() {

        val completable = Completable.create { completable ->
            //do logic here
            val success = true

            if (success) {
                completable.onComplete()
            } else {
                val someException = IllegalArgumentException("some fake error")
                completable.onError(someException)
            }
        }

        completable.subscribe({
            //handle on complete
            println("👻 Completable completed")
        }, { error ->
            //do something for error
        }).disposedBy(bag)

    }

    fun traits_maybe() {
        val maybe = Maybe.create<String> { maybe ->
            //do something
            val success = true
            val hasResult = true


            if (success) {
                if (hasResult) {
                    maybe.onSuccess("some result")
                } else {
                    maybe.onComplete()
                }
            } else {
                val someException = IllegalArgumentException("some fake error")
                maybe.onError(someException)
            }
        }

        maybe.subscribe({ result ->
            //do something with result
            println("👻 Maybe - result: ${ result }")
        }, { error ->
            //do something with the error
        }, {
            //do something about completing
            println("👻 Maybe - completed")
        }).disposedBy(bag)
    }
}