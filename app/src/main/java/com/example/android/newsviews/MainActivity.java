package com.example.android.newsviews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Code for parsing inspired by:
 *
 * http://www.tutorialspoint.com/android/android_json_parser.htm
 *
 * Boilerplate for input stream handling:
 *
 * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
 */

public class MainActivity extends AppCompatActivity {

    private static final int MAX_RESULTS = 40;
    private TextView tv;
    // handles refresh of news feed
    private Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        public void run()

        {
            lookUpArticles(findViewById(R.id.list));
            // 20 second delay.  reduce to avoid API shutdown :)
            MainActivity.this.mHandler.postDelayed(mRunnable, 20000);
        }

    };

    ArrayList<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.directions_and_status_textview);

        articleList = new ArrayList<>();

        ArticleAdapter itemsAdapter = new ArticleAdapter(this, articleList, R.color.colorPrimary);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        this.mHandler = new Handler();
        mRunnable.run();
    }


    public void lookUpArticles(View view) {
        // clear the list in case it's populated from prior search
        articleList.clear();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            new DownloadWebpageTask().execute("http://content.guardianapis.com/technology?api-key=test");

        } else {
            // display error
            tv.setText(R.string.connection_failed);
        }
    }

    /*
    Uses AsyncTask to create a task away from the main UI thread. Once the connection
    has been established, the AsyncTask downloads the contents of the webpage as
    an InputStream. Finally, the InputStream is converted into a string, which is
    displayed in the UI by the AsyncTask's onPostExecute method.
    */
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return callApiFromUrl(urls[0]);
            } catch (IOException e) {
                return getString(R.string.non_200_response);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            parseReply(result);
        }
    }

    /*
    Given a URL, establishes an HttpUrlConnection and retrieves
    the web page content as a InputStream, which it returns as
    a string.
    */
    private String callApiFromUrl(String myurl) throws IOException {
        InputStream responseStream = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(getLocalClassName(), "The response is: " + response);
            responseStream = conn.getInputStream();

            // Convert the InputStream into a string
            return buildJSONResponseString(responseStream);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (responseStream != null) {
                responseStream.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String buildJSONResponseString(InputStream stream) throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String read;

        while ((read = br.readLine()) != null) {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }


    private void parseReply(String reply) {
        String data = "";
        try {
            JSONObject jsonResponseObject = new JSONObject(reply).getJSONObject("response");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray articleArray = jsonResponseObject.getJSONArray("results");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject articleObject = articleArray.getJSONObject(i);
                String title = articleObject.getString("webTitle");
                String pubDate = articleObject.getString("webPublicationDate");
                String articleURL = articleObject.getString("webUrl");

                articleList.add(buildArticle(title, articleURL, R.drawable.ic_bookmark_border_white_24dp));
            }
            tv.setText(String.format(Locale.getDefault(), "Here are your results!\n%d articles found", articleArray.length()));
        } catch (JSONException e) {
            tv.setText(R.string.no_results_found_search);
        }
    }

    private Article buildArticle(String title, String url, int imgID) {
        return new Article(url, title, url, imgID);
    }
}
