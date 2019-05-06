package com.padcmyanmar.padc7.mmnews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.padcmyanmar.padc7.mmnews.R;
import com.padcmyanmar.padc7.mmnews.adapters.NewsAdapter;
import com.padcmyanmar.padc7.mmnews.components.SmartRecyclerView;
import com.padcmyanmar.padc7.mmnews.components.SmartScrollListener;
import com.padcmyanmar.padc7.mmnews.data.vos.NewsVO;
import com.padcmyanmar.padc7.mmnews.delegates.NewsItemDelegate;
import com.padcmyanmar.padc7.mmnews.views.pods.EmptyViewPod;
import com.padcmyanmar.padc7.mmnews.views.pods.LoginUserViewPod;
import com.padcmyanmar.padc7.mvp.presenters.NewsListPresenter;
import com.padcmyanmar.padc7.mvp.views.NewsListView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NewsItemDelegate, NewsListView {

    private BottomSheetBehavior<NestedScrollView> mBottomSheetBehavior;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_news)
    SmartRecyclerView rvNews;

    @BindView(R.id.vp_empty)
    EmptyViewPod vpEmpty;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsAdapter mNewsAdapter;

    private SmartScrollListener mSmartScrollListener;

    NewsListPresenter mNewsListPresenter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.title_latest_news);

        setSupportActionBar(mToolbar);

        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_lastest_news:
                        Toast.makeText(getApplicationContext(), getString(R.string.title_latest_news), Toast.LENGTH_SHORT).show();
                        mToolbar.setTitle(R.string.title_latest_news);
                        break;
                    case R.id.menu_news_just_for_you:
                        Toast.makeText(getApplicationContext(), getString(R.string.title_news_just_for_you), Toast.LENGTH_SHORT).show();
                        mToolbar.setTitle(R.string.title_news_just_for_you);
                        break;
                    case R.id.menu_favorite_news:
                        Toast.makeText(getApplicationContext(), getString(R.string.title_favorite_news), Toast.LENGTH_SHORT).show();
                        mToolbar.setTitle(R.string.title_favorite_news);
                        break;
                }
                for (int index = 0; index < mNavigationView.getMenu().size(); index++) {
                    mNavigationView.getMenu().getItem(index).setChecked(false);
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewsListPresenter.onListEndReach();
                /*mNewsModel.getNews(new NewsModel.NewsDelegate() {
                    @Override
                    public void onSuccess(List<NewsVO> newsList) {
                        mNewsAdapter.setNewData(newsList);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(String msg) {

                    }
                }, true);*/
            }
        });

        mSmartScrollListener = new SmartScrollListener(new SmartScrollListener.OnSmartScrollListener() {
            @Override
            public void onListEndReach() {
                Toast.makeText(getApplicationContext(), "onListEndReach", Toast.LENGTH_LONG).show();
                mNewsListPresenter.onListEndReach();
               /* mNewsModel.loadMoreNews(new NewsModel.NewsDelegate() {
                    @Override
                    public void onSuccess(List<NewsVO> newsList) {
                        //mNewsAdapter.appendNewData(newsList);
                        mNewsAdapter.setNewData(newsList);
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });*/
            }
        });

        rvNews.addOnScrollListener(mSmartScrollListener);
        rvNews.setEmptyView(vpEmpty);
        rvNews.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        mNewsAdapter = new NewsAdapter(this);
        rvNews.setAdapter(mNewsAdapter);

        //bindNews();
        mNewsListPresenter.onUIReady();
        NestedScrollView nsvBottomSheet = findViewById(R.id.nsv_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(nsvBottomSheet);

        mBottomSheetBehavior.setPeekHeight(0);

        if (!mUserModel.isUserLogin()) {
            //User hasn't login
            navigateToLoginScreen();
            return;
        }

        LoginUserViewPod vpLoginUser = (LoginUserViewPod) mNavigationView.getHeaderView(0);
        vpLoginUser.setData(mUserModel.getLoginUser());
        vpLoginUser.setDelegate(new LoginUserViewPod.LoginUserViewPodDelegate() {
            @Override
            public void onTapLogout() {
                mUserModel.onUserLogout();
                navigateToLoginScreen();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onTapFab(View view) {
        /*
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
                */

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

   /* @Override
    public void onTapNewsItem() {
        Intent intent = NewsDetailsActivity.newIntent(getApplicationContext());
        startActivity(intent);
    }*/

    /*private void bindNews() {
        List<NewsVO> news = mNewsModel.getNews(new NewsModel.NewsDelegate() {
            @Override
            public void onSuccess(List<NewsVO> newsList) {
                mNewsAdapter.setNewData(newsList);
            }

            @Override
            public void onError(String msg) {
                //Toast Msg.
            }
        }, true);

        if (news != null) {
            mNewsAdapter.setNewData(news);
        }
    }
*/
    private void navigateToLoginScreen() {
        Intent intent = LoginActivity.newIntent(getApplicationContext());
        startActivity(intent);
    }


    @Override
    public void displayNewsList(@NotNull List<NewsVO> newsList) {
        mNewsAdapter.setNewData(newsList);
    }

    @Override
    public void showLoadMoreNews(@NotNull List<NewsVO> newsList) {
        mNewsAdapter.setNewData(newsList);
    }



    @Override
    public void showRefreshNews(@NotNull List<NewsVO> newsList) {
        mNewsAdapter.setNewData(newsList);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayFailToLoadData(@NotNull String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void displayNewsDetails(@NotNull NewsVO news) {
        Intent intent = NewsDetailsActivity.newIntent(getApplicationContext());
        intent.putExtra("newsId", news.getNewsId());
        startActivity(intent);
    }

    @Override
    public void onTapNewsItem(NewsVO news) {
        mNewsListPresenter.onTapNewsItem(news);
    }
}
