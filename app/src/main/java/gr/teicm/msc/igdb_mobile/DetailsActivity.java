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




        buttonVisitWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
    }

}
