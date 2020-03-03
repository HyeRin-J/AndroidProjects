package com.example.clothes.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clothes.MainActivity.Companion.clothes
import kotlin.random.Random

class HomeViewModel : ViewModel() {
    var images = MutableLiveData<Array<ByteArray?>>().apply {
        value = arrayOfNulls(4)
    }

    fun randomImageSelect() : Array<Int>{
        val temp = arrayOfNulls<ByteArray?>(4)
        val resultArray = arrayOf(0, 0, 0, 0)
        for(i in 0..3) {
            if (clothes[i].isNotEmpty()) {
                val index = Random.nextInt(clothes[i].size)
                resultArray[i] = index
                temp[i] = clothes[i][index].image
            }
        }
        if (images.value != null)   images.value = temp
        return resultArray
    }
}