package com.jonbott.learningrxjava.ModelLayer.NetworkLayer

import com.jonbott.datalayerexample.DataLayer.NetworkLayer.EndpointInterfaces.JsonPlaceHolder
import com.jonbott.datalayerexample.DataLayer.NetworkLayer.Helpers.ServiceGenerator
import com.jonbott.learningrxjava.Common.EmptyDescriptionException
import com.jonbott.learningrxjava.Common.NullBox
import com.jonbott.learningrxjava.Common.StringLambda
import com.jonbott.learningrxjava.Common.VoidLambda
import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import com.jonbott.learningrxjava.ModelLayer.Entities.Person
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zip
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.io.IOException


typealias MessageLambda = (Message?)->Unit
typealias MessagesLambda = (List<Message>?)->Unit

class NetworkLayer {
    companion object { val instance = NetworkLayer() }

    private val placeHolderApi: JsonPlaceHolder

    init {
        placeHolderApi = ServiceGenerator.createService(JsonPlaceHolder::class.java)
    }

    //region End Point - Fully Rx

    fun getMessageRx(articleId:String): Single<Message> {
        return placeHolderApi.getMessageRx(articleId)
    }

    fun getMessagesRx(): Single<List<Message>>{
        return placeHolderApi.getMessagesRx()
    }

    fun postMessageRx(message: Message):Single<Message>{
        return placeHolderApi.postMessageRx(message)
    }

    //region End Point - SemiRx Way

//    region Task Example

//    Make one observable for each person on a list
    fun loadInfoFor(people: List<Person>): Observable<List<String>>{
//    Foreach person make a network call
    val networkObservable = people.map(::buildGetInfoNetworkCallFor)
//     When all server results have returned zip observables into a single observable

    return networkObservable.zip{list->
        list.filter { box-> box.value != null }
                .map { it.value!! }

    }

}

//    Wrap task in Reactive Observable
//    This pattern is used often for units of work
    private fun buildGetInfoNetworkCallFor(person: Person): Observable<NullBox<String>>{

    return Observable.create<NullBox<String>>{observer->
//      Execute Request - Do actual work here
        getInfoFor(person){result->

            result.fold({info->
                observer.onNext(info)
                observer.onComplete()
            }, {error->
//                Do something with error or just pass it on
                observer.onError(error)
            })
        }
    }.onErrorReturn { NullBox(null) }
}

//    Create a Network Task
    fun getInfoFor(person: Person, finished:(com.github.kittinunf.result.Result<NullBox<String>, Exception>)->Unit){
//        Execute on Background Thread
//        Do your task here
        launch {
            println("start network call: $person")
//            to milliseconds
            val randomTime = person.age * 1000
            delay(randomTime)
            println("finished network call: $person")

//            just randomly make odd people null
//            var result = com.github.kittinunf.result.Result.of (NullBox(person.toString()))

//            Adding Nulls
            val isEven = person.age % 2 ==0
            var result = if (isEven) com.github.kittinunf.result.Result.of(NullBox(person.firstName))
            else com.github.kittinunf.result.Result.Companion.of(NullBox<String>(null))

//            Adding Execeptions
            if (person.age>3){
                result = com.github.kittinunf.result.Result.of { throw EmptyDescriptionException ("This person's age is odd")}
            }


//            Result.Failure

            finished(result)
        }
    }





    fun getMessages(success: MessagesLambda, failure: StringLambda) {
        val call = placeHolderApi.getMessages()

        call.enqueue(object: Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>?, response: Response<List<Message>>?) {
                val article = parseResponse(response)
                success(article)
            }

            override fun onFailure(call: Call<List<Message>>?, t: Throwable?) {
                println("Failed to GET Message: ${ t?.message }")
                failure(t?.localizedMessage ?: "Unknown error occurred")
            }
        })
    }

    fun getMessage(articleId: String, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.getMessage(articleId)

        call.enqueue(object: Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseResponse(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to GET Message: ${ t?.message }")
                failure()
            }
        })
    }

    fun postMessage(message: Message, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.postMessage(message)

        call.enqueue(object: Callback<Message>{
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseResponse(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to POST Message: ${ t?.message }")
                failure()
            }
        })
    }

    private fun <T> parseResponse(response: Response<T>?): T? {
        val article = response?.body() ?: null

        if (article == null) {
            parseResponseError(response)
        }

        return article
    }

    private fun <T> parseResponseError(response: Response<T>?) {
        if(response == null) return //can't do anything here

        val responseBody = response.errorBody()

        if(responseBody != null) {
            try {
                val text = "responseBody = ${ responseBody.string() }"
                println("$text")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val text = "responseBody == null"
            println("$text")
        }
    }

    //endregion

}