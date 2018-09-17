package com.hahafather007.tvdemo.utils

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.databinding.ObservableList
import com.annimon.stream.function.Consumer
import io.reactivex.Observable

object RxField {
    private fun <T> from(observable: android.databinding.Observable, getter: () -> T?): Observable<T> {
        return Observable.create { emitter ->
            val invoke = getter.invoke()
            if (invoke != null) {
                emitter.onNext(invoke)
            }

            val callback = object : android.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: android.databinding.Observable, propertyId: Int) {
                    val invoke2 = getter.invoke()
                    if (invoke2 != null) {
                        emitter.onNext(invoke2)
                    }
                }
            }
            emitter.setCancellable { observable.removeOnPropertyChangedCallback(callback) }
            observable.addOnPropertyChangedCallback(callback)
        }
    }

    fun <T> of(observable: ObservableField<T>): Observable<T?> {
        return from(observable) { observable.get() }
    }

    fun of(observable: ObservableBoolean): Observable<Boolean> {
        return from(observable) { observable.get() }
    }

    fun of(observable: ObservableInt): Observable<Int> {
        return from(observable) { observable.get() }
    }

    fun <T> of(observable: ObservableList<T>): Observable<List<T>> {
        return Observable.create { emitter ->
            emitter.onNext(observable)
            val callback = ObservableListCallback<T>(Consumer { emitter.onNext(it) })

            emitter.setCancellable { observable.removeOnListChangedCallback(callback) }
            observable.addOnListChangedCallback(callback)
        }
    }

    class ObservableListCallback<T>(private val callback: Consumer<List<T>>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {
        override fun onChanged(sender: ObservableList<T>) {
            callback.accept(sender)
        }

        override fun onItemRangeChanged(sender: ObservableList<T>,
                                        positionStart: Int, itemCount: Int) {
            callback.accept(sender)
        }

        override fun onItemRangeInserted(sender: ObservableList<T>,
                                         positionStart: Int, itemCount: Int) {
            callback.accept(sender)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>,
                                      fromPosition: Int, toPosition: Int, itemCount: Int) {
            callback.accept(sender)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>,
                                        positionStart: Int, itemCount: Int) {
            callback.accept(sender)
        }
    }
}
