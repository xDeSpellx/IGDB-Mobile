package gr.teicm.msc.igdb_mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListActivity extends AppCompatActivity
{
    TextView textViewInfo;
    ListView listViewBooks;
    private static final String TAG = "ListActivity";
    String queryURL;
    String finalURL;
    String contents;
    JSONArray jsonArray;
    ListView listview ;
    ArrayList<HashMap<String, String>> gameList;
    ListAdapter adapter;
    private ProgressBar progressSpin;
    TextView noGames ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        Intent intent = getIntent();
        queryURL = intent.getStringExtra("queryString");
        finalURL = queryURL+"&format=json";
        listview = (ListView) findViewById(R.id.listViewGames);
        progressSpin = (ProgressBar)findViewById(R.id.loadingSpin);
        noGames = (TextView)findViewById(R.id.noGamesLabel);
        gameList = new ArrayList<>();
        backgroundLoader obj = new backgroundLoader();
        Thread t1 = new Thread(obj);
        t1.start();
        progressSpin.setVisibility(View.VISIBLE);
        setTitle("IGDB Game List");
    }

        //Parse the JSON object from network
        public class backgroundLoader extends Thread
        {
            public void run()
            {
                try
                {
                    contents = NetworkUtils.getFileContentsFromFromUrl(finalURL);
                } finally
                {
                    try
                    {
                        jsonArray = new JSONArray(contents);
                        populateList();

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

    //Creates and assigns the adapter to listview
    public void populateList() throws JSONException
    {
        for (int i = 0 ; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            String gameTitle = obj.getString("GameTitle");
            HashMap<String, String> game = new HashMap<>();
            game.put("gameTitle", gameTitle);
            gameList.add(game);
        }

            adapter = new SimpleAdapter(
                                        getApplicationContext(),
                                        gameList,
                                        R.layout.list_item,
                                        new String[]{"gameTitle"},
                                        new int[]{R.id.gameTitle}
                                        );

            //Only the main thread can change UI elements
            runOnUiThread(new Runnable()
            {
            @Override
            public void run()
            {
                listview.setAdapter(adapter);
                progressSpin.setVisibility(View.GONE);

                if (adapter.isEmpty())
                {
                    noGames.setVisibility(View.VISIBLE);
                }
            }
            });
    }

    @Override
    protected void onPause()
    {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
    }
}
