package gr.teicm.msc.igdb_mobile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    TextView textViewTitle;
    TextView textViewAuthor;
    TextView textViewGenre;
    Button buttonVisitWebsite;

    ImageView imageViewCover;
    ImageLoader imageLoader;


    HashMap<String, Object> book = null;

    private void findViews() {
        textViewTitle = (TextView)findViewById(R.id.book_details_title);
        textViewAuthor = (TextView)findViewById(R.id.book_details_author);
        textViewGenre = (TextView)findViewById(R.id.book_details_genre);
        buttonVisitWebsite = (Button)findViewById(R.id.buttonVisitWebsite);

        imageViewCover = (ImageView)findViewById(R.id.imageViewCover);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Animation when this Activity appears
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);

        findViews();

        Intent intent = getIntent();
        int bookPosition = intent.getIntExtra(DataStore.KEY_POSITION, 0);

        book =  DataStore.Books.get(bookPosition);
        String bookTitle = (String)book.get(DataStore.KEY_TITLE);
        String bookAuthor = (String)book.get(DataStore.KEY_AUTHOR);
        String bookGenreName = (String)book.get(DataStore.KEY_GENRENAME);
        textViewTitle.setText(bookTitle);
        textViewAuthor.setText(bookAuthor);
        textViewGenre.setText(bookGenreName);

        String bookCoverUrl = (String)book.get(DataStore.KEY_COVERURL);
        imageLoader = new ImageLoader(getApplicationContext());
        imageLoader.DisplayImage(bookCoverUrl, imageViewCover);


        buttonVisitWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookAmazonUrl = (String)book.get(DataStore.KEY_AMAZONURL);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookAmazonUrl));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
    }

}
