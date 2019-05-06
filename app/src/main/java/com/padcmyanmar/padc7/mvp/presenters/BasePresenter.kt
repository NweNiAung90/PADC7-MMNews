package com.padcmyanmar.padc7.mvp.presenters

abstract class BasePresenter {
    fun onCreate(){

    }
    abstract fun onStart()
    abstract fun onStop()
    fun onDestroy(){
    }
}
