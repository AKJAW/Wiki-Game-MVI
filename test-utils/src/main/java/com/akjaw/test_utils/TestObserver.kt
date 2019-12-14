package com.akjaw.test_utils

import io.reactivex.observers.TestObserver
import java.lang.IndexOutOfBoundsException

fun <T> TestObserver<T>.assertLastValue(predicate: (T) -> Boolean){
    assertValueAt(valueCount() - 1){
        predicate(it)
    }
}

fun <T> TestObserver<T>.assertSecondToLastValue(predicate: (T) -> Boolean){
    assertValueAt(valueCount() - 2){
        predicate(it)
    }
}