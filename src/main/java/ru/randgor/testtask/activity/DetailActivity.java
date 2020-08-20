package ru.randgor.testtask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.randgor.testtask.R;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_view)
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");

        if (url == null){
            Toast.makeText(getApplicationContext(), "No URL found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        webView.loadUrl(url);
    }
}