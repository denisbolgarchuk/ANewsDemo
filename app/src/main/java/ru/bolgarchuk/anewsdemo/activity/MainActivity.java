package ru.bolgarchuk.anewsdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bolgarchuk.anewsdemo.R;
import ru.bolgarchuk.anewsdemo.adapter.TopNewsAdapter;
import ru.bolgarchuk.anewsdemo.db.DatabaseHelper;
import ru.bolgarchuk.anewsdemo.listener.EndlessRecyclerOnScrollListener;
import ru.bolgarchuk.anewsdemo.model.TopNews;
import ru.bolgarchuk.anewsdemo.network.Api;

public class MainActivity extends AppCompatActivity implements Callback<List<TopNews>> {
    public final static String COUNTRY = "ru";
    private TopNewsAdapter mNewsAdapter;
    private RecyclerView mNewsRecyclerView;
    private ProgressBar mNewsLoadingProgress;
    private TextView mLoadFailedText;
    private EndlessRecyclerOnScrollListener mEndlessScrollListener;
    private List<TopNews> mNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsRecyclerView = (RecyclerView) findViewById(R.id.top_news_recycler_view);
        mNewsLoadingProgress = (ProgressBar) findViewById(R.id.top_news_loading_progress);
        mLoadFailedText = (TextView) findViewById(R.id.top_news_load_failed_text);
        mNews = new ArrayList<>();

        initLoad();
    }

    private void initLoad() {
        Call<List<TopNews>> call = Api.getApiService().topRegionNews(COUNTRY);
        call.enqueue(MainActivity.this);
        mNewsLoadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Call<List<TopNews>> call, Response<List<TopNews>> response) {
        mNewsLoadingProgress.setVisibility(View.GONE);
        List<TopNews> topNews = response.body();

        if (mNewsAdapter == null) {
            initRecyclerView(true);
            mNewsRecyclerView.setAdapter(mNewsAdapter);
            mNewsRecyclerView.addOnScrollListener(mEndlessScrollListener);
        } else {
            mNewsRecyclerView.setVisibility(View.VISIBLE);
            mNewsAdapter.addItems(topNews);
            mNewsAdapter.notifyDataSetChanged();
        }
        mEndlessScrollListener.setLastId(topNews.get(topNews.size() - 1).getId());

        DatabaseHelper dbHelper = OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class);
        try {
            Dao<TopNews, Integer> newsDao = dbHelper.getNewsDao();
            if (mNews.isEmpty()) {
                dbHelper.clearNewsTable();
            }
            for (TopNews topNewsPiece : topNews) {
                newsDao.create(topNewsPiece);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mNews.addAll(topNews);
    }

    @Override
    public void onFailure(Call<List<TopNews>> call, Throwable t) {
        mNewsLoadingProgress.setVisibility(View.GONE);
        if (mNews.isEmpty()) {
            DatabaseHelper dbHelper = OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class);
            try {
                Dao<TopNews, Integer> newsDao = dbHelper.getNewsDao();
                mNews = newsDao.queryForAll();

                if (!mNews.isEmpty()) {
                    initRecyclerView(false);
                    mNewsRecyclerView.setAdapter(mNewsAdapter);
                } else {
                    showLoadFailed();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLoadFailed() {
        mLoadFailedText.setVisibility(View.VISIBLE);
        mLoadFailedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadFailedText.setVisibility(View.GONE);
                initLoad();
            }
        });
    }

    private void initRecyclerView(boolean isEndless) {
        mNewsRecyclerView.setVisibility(View.VISIBLE);
        mNewsAdapter = new TopNewsAdapter(mNews, MainActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mNewsRecyclerView.setLayoutManager(mLayoutManager);
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isEndless) {
            mEndlessScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int maxId) {
                    Call<List<TopNews>> call = Api.getApiService().topRegionNewsPage(COUNTRY, maxId);
                    call.enqueue(MainActivity.this);
                }
            };
        }
    }
}