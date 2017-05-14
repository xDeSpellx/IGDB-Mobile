package gr.teicm.msc.igdb_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity
{
    TextView textViewInfo;
    ListView listViewBooks;
    private static final String TAG = "ListActivity";

    private void findViews()
    {
        textViewInfo = (TextView)findViewById(R.id.textViewInfo);
        listViewBooks = (ListView)findViewById(R.id.listViewBooks);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Animation when this Activity appears
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        //get user filters from Intent

        Intent intent = getIntent();
        String queryURL = intent.getStringExtra("queryString");
        Log.v(TAG,queryURL);

       /* String filterTitle = intent.getStringExtra("TITLE");
        int filterGenreId = intent.getIntExtra("GENREID", 0);
        findViews();
        //show user filters for information
        String message = String.format("Author: %s\nTitle: %s\nGenreId: %d", filterAuthor,filterTitle, filterGenreId);
        textViewInfo.setText(message);
        //show all genres on our list

        DataStore.LoadBooks(filterAuthor, filterTitle, filterGenreId);
        //COMPLEX OBJECT BINDING
        LazyAdapter booksAdapter = new LazyAdapter(this, DataStore.Books);
        listViewBooks.setAdapter(booksAdapter);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
                detailsIntent.putExtra(DataStore.KEY_POSITION, position);
                startActivity(detailsIntent);
            }
        });*/


    }

    @Override
    protected void onPause()
    {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
    }

}
