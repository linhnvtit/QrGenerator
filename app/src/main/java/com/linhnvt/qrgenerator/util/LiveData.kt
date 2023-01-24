package com.linhnvt.qrgenerator.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

internal inline fun <T> LiveData<T>.observeNotNull(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) = observe(lifecycleOwner) { data ->
    data?.let {
        observer(data)
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

internal inline fun <T> LiveData<T>.observeOnce(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) = observe(lifecycleOwner, object : Observer<T> {
    override fun onChanged(t: T) {
        observer(t)
        removeObserver(this)
    }
})