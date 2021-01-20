package com.jonbott.datalayerexample.DataLayer.NetworkLayer.EndpointInterfaces

import com.jonbott.learningrxjava.ModelLayer.Entities.Message
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPlaceHolder {
    //region Regular Web Methods

    @GET("/posts")
    fun getMessages(): Call<List<Message>>

    @GET("/posts/{articleId}")
    fun getMessage(@Path("articleId") articleId: String): Call<Message>

    @POST("/posts")
    fun postMessage(@Body message: Message): Call<Message>

    //endregion

    //region RxMethods

    @GET("/posts")
    fun getMessagesRx(): Single<List<Message>>

    @GET("/posts/{articleId}")
    fun getMessageRx(@Path("articleId") articleId: String): Single<Message>

    @POST("/posts")
    fun postMessageRx(@Body message: Message): Single<Message>

    //endregion

}