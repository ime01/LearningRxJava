package com.jonbott.learningrxjava.Activities.ReactiveUi.Complex

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jonbott.learningrxjava.Common.disposedBy
import com.jonbott.learningrxjava.Common.isEven
import com.jonbott.learningrxjava.R
import com.jonbott.learningrxjava.databinding.ActivityComplexUiBinding
import com.jonbott.learningrxjava.databinding.ItemReactiveUiBinding
import com.jonbott.learningrxjava.databinding.ItemReactiveUiOtherBinding
import com.minimize.android.rxrecycleradapter.OnGetItemViewType
import com.minimize.android.rxrecycleradapter.RxDataSource
import com.minimize.android.rxrecycleradapter.RxDataSourceSectioned
import com.minimize.android.rxrecycleradapter.ViewHolderInfo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.experimental.android.UI


//https://github.com/ahmedrizwan/RxRecyclerAdapter

private enum class CellType {
    ITEM,
    ITEM2
}

class ReactiveUIActivity : AppCompatActivity() {

    private val dataSet = (0..100).toList().map { it.toString() }
    private var bag = CompositeDisposable()
    lateinit var boundActivity: ActivityComplexUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex_ui)

        commonInit()
//        showSimpleBindingExample()
        showComplexBindingExample()
    }

//    region Stage 2
    private fun showComplexBindingExample(){
        val viewHolderInfoList = listOf(ViewHolderInfo(R.layout.item_reactive_ui, CellType.ITEM2.ordinal),
                ViewHolderInfo(R.layout.item_reactive_ui_other, CellType.ITEM.ordinal))

        val rxDataSourceSectioned = RxDataSourceSectioned(dataSet, viewHolderInfoList, object : OnGetItemViewType(){
            override fun getItemViewType(position: Int): Int {
//                determine cell type
                return if (position.isEven) CellType.ITEM.ordinal
                  else CellType.ITEM2.ordinal
            }
        })

        rxDataSourceSectioned
                .asObservable()
                .subscribe {
                    val ui = it.viewDataBinding ?: return@subscribe
                    val data = it.item

                    when(ui){
                      is ItemReactiveUiBinding -> ui.textViewItem.text = "Cell Type 1: $data"
                        is ItemReactiveUiOtherBinding -> ui.textViewItem2.text = "Cell Type 2: $data"
                    }
                }

        rxDataSourceSectioned.bindRecyclerView(boundActivity.reactiveUIRecyclerView)

    }

    fun commonInit(){
       boundActivity = DataBindingUtil.setContentView(this, R.layout.activity_complex_ui)
        boundActivity.reactiveUIRecyclerView.layoutManager = LinearLayoutManager(this)
    }
//    region Stage 1
    fun showSimpleBindingExample(){
        val rxDataSource = RxDataSource<ItemReactiveUiBinding, String>(R.layout.item_reactive_ui, dataSet)
        rxDataSource.bindRecyclerView(boundActivity.reactiveUIRecyclerView)

        rxDataSource.map { it.toUpperCase() }
                .asObservable()
                .subscribe{
                    val ui = it.viewDataBinding ?: return@subscribe
                    val data = it.item

                    ui.textViewItem.text = data
                }.disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}