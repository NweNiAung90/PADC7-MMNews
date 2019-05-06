package com.padcmyanmar.padc7.mvp.presenters

import android.widget.Toast
import com.padcmyanmar.padc7.mmnews.adapters.NewsAdapter
import com.padcmyanmar.padc7.mmnews.data.models.NewsModel
import com.padcmyanmar.padc7.mmnews.data.models.NewsModelImpl
import com.padcmyanmar.padc7.mmnews.data.vos.NewsVO
import com.padcmyanmar.padc7.mvp.views.NewsListView

class NewsListPresenterImpl(private val newsListView: NewsListView) :BasePresenter(), NewsListPresenter {

    private val mNewsModel : NewsModel = NewsModelImpl.getInstance()
    //bindNews()
    override fun onUIReady() {
        val news : MutableList<NewsVO>  = mNewsModel.getNews(object : NewsModel.NewsDelegate{
            override fun onSuccess(newsList: MutableList<NewsVO>?) {
                newsListView.displayNewsList(newsList!!)
            }

            override fun onError(msg: String?) {
                newsListView.displayFailToLoadData(msg!!)
            }

        },true)
        if (news != null) {
            newsListView.displayNewsList(news)
        }

    }

    override fun onListEndReach() {
        mNewsModel.loadMoreNews(object : NewsModel.NewsDelegate{
            override fun onSuccess(newsList: MutableList<NewsVO>?) {
                newsListView.showLoadMoreNews(newsList!!)
            }

            override fun onError(msg: String?) {
                newsListView.displayFailToLoadData(msg!!)
            }

        })
    }

    override fun onRefreshNews() {
        mNewsModel.getNews(object : NewsModel.NewsDelegate{
            override fun onSuccess(newsList: MutableList<NewsVO>?) {
                newsListView.showRefreshNews(newsList!!)
            }

            override fun onError(msg: String?) {
                newsListView.displayFailToLoadData(msg!!)
            }

        },true)
    }


   override fun onTapNewsItem(news: NewsVO?) {
       newsListView.displayNewsDetails(news!!)
   }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}