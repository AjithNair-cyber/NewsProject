package com.example.newsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String url = "https://newsapi.org/v2/";
    private static final String apiKey = Env.getApi();
    private static final Env env = new Env();
    private ArrayList<Articles> articlesArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RetroInterface retroInterface;
    private ExampleAdapter adapter;
    private ProgressBar progressBar;
    private EditText editText;
    private Map<String, String> parameters = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        retroBuilder();
        parameters.put("country", "in");
        parameters.put("apiKey", apiKey);
        parameters.put("pageSize", "100");
        paramsPasser("category", "general");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator  = getMenuInflater();
        inflator.inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.business):
                paramsPasser("category", "business");
                return true;
            case (R.id.sports):
                paramsPasser("category", "sports");
                return true;
            case (R.id.entertainment):
                paramsPasser("category", "entertainment");
                return true;
            case (R.id.general):
                paramsPasser("category", "general");
                return true;
            case (R.id.india):
                paramsPasser("country", "in");
                return true;
            case (R.id.china):
                paramsPasser("country", "cn");
                return true;
            case (R.id.france):
                paramsPasser("country", "fr");
                return true;
            case (R.id.germany):
                paramsPasser("country", "de");
                return true;
            case (R.id.usa) :
                paramsPasser("country", "us");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void apiActivity() {
        Call<OuterClass> call = retroInterface.getNews(parameters);

        call.enqueue(new Callback<OuterClass>() {
            @Override
            public void onResponse(Call<OuterClass> call, Response<OuterClass> response) {
                Log.d("Main", " " + response.code());
                OuterClass outerClass = response.body();
                articlesArrayList = outerClass.getArticles();
                progressBar.setVisibility(View.GONE);
                recyclerViewBuilder();


            }

            @Override
            public void onFailure(Call<OuterClass> call, Throwable t) {
                Log.d("Main", " " + t);

            }
        });
    }

    private void retroBuilder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroInterface = retrofit.create(RetroInterface.class);
    }

    private void paramsPasser(String key, String category) {
        parameters.remove(key);
        parameters.put(key, category);
        parameters.put("apiKey", apiKey);
        parameters.put("pageSize", "100");
        Log.d("Parameters", "" + parameters);
        progressBar.setVisibility(View.VISIBLE);
        apiActivity();
    }

    private void recyclerViewBuilder() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        TextView textView = findViewById(R.id.error);
        if(articlesArrayList==null){
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            layoutManager = new LinearLayoutManager(this);
            adapter = new ExampleAdapter(articlesArrayList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String url = articlesArrayList.get(position).getUrl();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                @Override
                public void onItemDelete(int position) {
                    articlesArrayList.remove(position);
                    adapter.notifyItemRemoved(position);
                }

                @Override
                public void onItemShare(int position) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    i.putExtra(Intent.EXTRA_TEXT, articlesArrayList.get(position).getUrl());
                    startActivity(Intent.createChooser(i, "Share URL"));
                }
            });
        }
    }
}