package com.hahafather007.tvdemo.common

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hahafather007.tvdemo.common.DataBindingItemViewBinder.OnBindItem
import io.reactivex.functions.BiFunction
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

object Binding {
    @JvmStatic
    @BindingAdapter("itemLayout", "onBindItem")
    fun setAdapter(view: RecyclerView, resId: Int, onBindItem: OnBindItem<Any, ViewDataBinding>) {
        val adapter = view.getOrCreateAdapter()

        adapter.register(Any::class.java, DataBindingItemViewBinder(resId, onBindItem))
    }

    @JvmStatic
    @BindingAdapter("items")
    fun <T> setItems(view: RecyclerView, items: List<T>) {
        val adapter = view.getOrCreateAdapter()

        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    /**
     * 创建RecyclerView的适配器
     */
    private fun RecyclerView.getOrCreateAdapter(): MultiTypeAdapter {
        if (adapter !is MultiTypeAdapter) {
            adapter = MultiTypeAdapter()
        }

        return adapter as MultiTypeAdapter
    }
}

class DataBindingViewHolder<T : ViewDataBinding>(val dataBinding: T) : RecyclerView.ViewHolder(dataBinding.root)

class DataBindingItemViewBinder<T, DB : ViewDataBinding> : ItemViewBinder<T, DataBindingViewHolder<DB>> {

    private val delegate: Delegate<T, DB>

    constructor(delegate: Delegate<T, DB>) {
        this.delegate = delegate
    }

    constructor(factory: BiFunction<LayoutInflater, ViewGroup, DB>,
                binder: OnBindItem<T, DB>) : this(SimpleDelegate(factory, binder))

    constructor(@LayoutRes resId: Int, binder: OnBindItem<T, DB>) : this(
            BiFunction { inflater: LayoutInflater, parent: ViewGroup ->
                DataBindingUtil.inflate<DB>(inflater, resId, parent, false)
            }, binder)

    override fun onCreateViewHolder(inflater: LayoutInflater,
                                    parent: ViewGroup): DataBindingViewHolder<DB> {
        return DataBindingViewHolder(delegate.onCreateDataBinding(inflater, parent))
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<DB>, item: T) {
        val binding = holder.dataBinding
        delegate.onBind(binding, item, holder.adapterPosition)
        binding.executePendingBindings()
    }

    interface Delegate<T, DB : ViewDataBinding> {
        fun onCreateDataBinding(inflater: LayoutInflater, parent: ViewGroup): DB

        fun onBind(dataBinding: DB, item: T, position: Int)
    }

    interface OnBindItem<T, DB : ViewDataBinding> {
        fun bind(dataBinding: DB, data: T, position: Int)
    }

    private class SimpleDelegate<T, DB : ViewDataBinding>(
            private val factory: BiFunction<LayoutInflater, ViewGroup, DB>,
            private val binder: OnBindItem<T, DB>) : Delegate<T, DB> {

        override fun onCreateDataBinding(inflater: LayoutInflater, parent: ViewGroup): DB {
            return factory.apply(inflater, parent)
        }

        override fun onBind(dataBinding: DB, item: T, position: Int) {
            binder.bind(dataBinding, item, position)
        }
    }
}
