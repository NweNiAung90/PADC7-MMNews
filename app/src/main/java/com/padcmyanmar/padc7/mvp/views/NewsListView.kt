package com.padcmyanmar.padc7.mvp.views

import com.padcmyanmar.padc7.mmnews.data.vos.NewsVO

interface NewsListView {

    fun displayNewsList(newsList : MutableList<NewsVO>)
    fun showLoadMoreNews(newsList : MutableList<NewsVO>)
    fun displayNewsDetails(news: NewsVO)
    fun showRefreshNews(newsList : MutableList<NewsVO>)
    fun displayFailToLoadData(message : String)
}