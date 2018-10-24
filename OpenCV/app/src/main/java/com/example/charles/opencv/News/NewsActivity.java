package com.example.charles.opencv.News;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.charles.opencv.Database.AchievementsDatabase;
import com.example.charles.opencv.HomeScreen;
import com.example.charles.opencv.News.Model.RSS;
import com.example.charles.opencv.R;
import com.google.gson.Gson;

public class NewsActivity extends AppCompatActivity {

    //RSS link
    private final String RSS_link = "https://backyardbirdlady.com/feed/";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    Toolbar toolbar;
    RecyclerView recyclerView;
    RSS rss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();
    }

    /***
     * Loads the RSS feed asynchronously
     */
    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog dialog = new ProgressDialog(NewsActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Loading News ...");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                DataHandler http = new DataHandler();
                result = http.GetData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                dialog.dismiss();
                rss = new Gson().fromJson(s, RSS.class);
                FeedAdapter adapter = new FeedAdapter(rss, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder get_data = new StringBuilder(RSS_to_Json_API);
        get_data.append(RSS_link);
        loadRSSAsync.execute(get_data.toString());
    }



}
/* TODO  UNIT TESTS
import com.example.charles.OpenCV.News.Feed;
import com.example.charles.OpenCV.News.FeedMessage;
import com.example.charles.OpenCV.News.RSSFeedParser;

public class ReadTest {
    public static void main(String[] args) {
        RSSFeedParser parser = new RSSFeedParser(
                "https://backyardbirdlady.com/feed/");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        for (FeedMessage message : feed.getMessages()) {
            System.out.println(message);

        }

    }
}
 */