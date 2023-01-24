package com.linhnvt.qrgenerator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    private val _currentPageIndex = MutableLiveData<Int>()
    val currentPageIndex: LiveData<Int>
        get() = _currentPageIndex

    private val _pageCount = MutableLiveData<Int>()
    val pageCount: LiveData<Int>
        get() = _pageCount

    init {
        _currentPageIndex.value = 0
        _pageCount.value = 0
    }

    fun changePageIndex(index: Int) = index.also { _currentPageIndex.value = it }
    fun changePageCount(index: Int) = index.also { _pageCount.value = it }

}