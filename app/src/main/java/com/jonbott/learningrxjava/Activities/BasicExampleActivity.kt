package com.jonbott.learningrxjava.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.ModelLayer.Entities.Posting
import com.jonbott.learningrxjava.R
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException


class BasicExampleActivity : AppCompatActivity() {

    private var bag = CompositeDisposable()

    //region Simple Network Layer

    interface JsonPlaceHolderService {
        @GET("posts/{id}")
        fun getPosts(@Path("id") id: String): Call<Posting>
    }

    private var retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()

    private var service = retrofit.create(JsonPlaceHolderService::class.java)

    //endregion

    //region Life Cycle Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_example)

        realSingleExample()
    }

    //endregion

    //region Rx Code

    private fun realSingleExample() {
        loadPostAsSingle().observeOn(AndroidSchedulers.mainThread())
                          .subscribeOn(Schedulers.io())
                          .subscribe({ posting ->
                              userIdTextView.text = posting.title
                              bodyTextView.text = posting.body
                          }, { error ->
                              println("❗️ an error occurred: ${ error.localizedMessage }")
                              userIdTextView.text = ""
                              bodyTextView.text = ""
                          }).disposedBy(bag)
    }

    private fun loadPostAsSingle(): Single<Posting> {
        return Single.create { observer ->
            //Simulate Network Delay
            Thread.sleep(2000)

            val postingId = 5
            service.getPosts(postingId.toString()).enqueue(object: Callback<Posting>{
                override fun onResponse(call: Call<Posting>?, response: Response<Posting>?) {
                    val posting = response?.body()

                    if(posting != null) {
                        observer.onSuccess(posting)
                    } else {
                        val e = IOException("An Unknown network error occurred")
                        observer.onError(e)
                    }
                }

                override fun onFailure(call: Call<Posting>?, t: Throwable?) {
                    val e = t ?: IOException("An Unknown network error occurred")
                    observer.onError(e)
                }
            })

        }
    }

    //endregion

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}








