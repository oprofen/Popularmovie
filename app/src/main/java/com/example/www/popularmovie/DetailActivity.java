package com.example.www.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private ImageView mPoster;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mPoster = findViewById(R.id.poster_value);
        mVoteAverage = findViewById(R.id.vote_average_value);
        mReleaseDate = findViewById(R.id.release_date_value);
        mOverview = findViewById(R.id.overview_value);

        Intent intent = getIntent();
        /*if(intent != null){

        }*/

        setTitle(intent.getStringExtra("title"));
        mVoteAverage.setText(intent.getStringExtra("rating"));
        mReleaseDate.setText(intent.getStringExtra("release_date"));
        mOverview.setText(intent.getStringExtra("overview"));
        String url = "http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("poster");
        Picasso.with(this)
                .load(url)
                .into(mPoster);
    }
}
