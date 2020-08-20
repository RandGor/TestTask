package ru.randgor.testtask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.randgor.testtask.DbHelper;
import ru.randgor.testtask.MyAdapter;
import ru.randgor.testtask.PaginationListener;
import ru.randgor.testtask.R;
import ru.randgor.testtask.SimpleRow;

import static ru.randgor.testtask.PaginationListener.PAGE_SIZE;
import static ru.randgor.testtask.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    MyAdapter adapter;
    MyAdapter.OnItemClickListener itemClickListener;
    PaginationListener paginationListener;
    ArrayList<SimpleRow> data;

    private RequestQueue mRequestQueue;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = PAGE_SIZE;
    private boolean isLoading = false;
    int itemCount = 0;

    private DbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialize();

        swipeRefresh.setOnRefreshListener(this);

        mRequestQueue = Volley.newRequestQueue(this);

        doApiCall();
    }

    public void initialize() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        Intent intent = new Intent(this, DetailActivity.class);

        mDbHelper = new DbHelper(this);

        itemClickListener = new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                switch (adapter.getItem(position).getType()) {
                    case MyAdapter.VIEW_TYPE_LOADING:
                        break;
                    case MyAdapter.VIEW_TYPE_NORMAL:
                        intent.putExtra("url", adapter.getItem(position).getUrl());
                        startActivity(intent);
                        break;
                    case MyAdapter.VIEW_TYPE_RETRY:
                        onRetry();
                        break;
                }
            }
        };

        paginationListener = new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                if (isLoading || isLastPage)
                    return;
                isLoading = true;
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };

        data = new ArrayList<>();

        adapter = new MyAdapter(data, itemClickListener, this);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(paginationListener);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private void onRetry() {
        mRequestQueue.cancelAll(this);
        isLoading = false;
        isLastPage = false;
        adapter.changeRetryToLoading();
        doApiCall();
    }

    private void doApiCall() {
        if (currentPage == PAGE_START)
            adapter.addLoading();

        String url = "https://newsapi.org/v2/everything?q=android&from=2019-04-00&sortBy=publishedAt&apiKey=26eddb253e7840f988aec61f2ece2907&page=" + currentPage;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");

                            adapter.removeLoading();

                            for (int i = 0; i < 5; i++) {
                                JSONObject article = jsonArray.getJSONObject(i);
                                SimpleRow row = new SimpleRow(article);
                                data.add(row);
                                mDbHelper.insertNew(row);
                                adapter.notifyItemInserted((currentPage - 1) * 5 + i);
                            }


                            swipeRefresh.setRefreshing(false);

                            if (currentPage < totalPage)
                                adapter.addLoading();
                            else
                                isLastPage = true;

                            isLoading = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefresh.setRefreshing(false);
                adapter.changeLoadingToRetry();
                error.printStackTrace();
            }
        });
        request.setTag(this);
        mRequestQueue.add(request);
    }

    @Override
    public void onRefresh() {
        mRequestQueue.cancelAll(this);
        itemCount = 0;
        currentPage = PAGE_START;
        isLoading = false;
        isLastPage = false;
        adapter.clear();
        adapter.isLoaderVisible = false;
        adapter.isRetryVisible = false;
        doApiCall();
    }
}
