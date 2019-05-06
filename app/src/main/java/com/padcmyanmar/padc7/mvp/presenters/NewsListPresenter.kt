package com.padcmyanmar.padc7.mvp.presenters

import com.padcmyanmar.padc7.mmnews.delegates.NewsItemDelegate

interface NewsListPresenter :NewsItemDelegate  {
    //bindData
    fun onUIReady()
    //onListEndReach
    fun onListEndReach()
    //swipeRefresh
    fun onRefreshNews()


}