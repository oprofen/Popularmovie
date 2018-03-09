package com.example.www.popularmovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.www.popularmovie.utils.MovieConnectionUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {

    public static int MOVIE_TYPE_POSITION = 0;
    public View mStatusNode;
    public TextView mStatusTextView;
    public ProgressBar mStatusProgressBar;
    private RecyclerView mRecycleViewMovies;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;
    private AsyncTask<URL, String, MovieItem[]> mTask;
    private String mErrorMessage;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusNode = findViewById(R.id.status);
        mStatusTextView = mStatusNode.findViewById(R.id.status_tv);
        mStatusProgressBar = mStatusNode.findViewById(R.id.status_pb);
        mRecycleViewMovies = findViewById(R.id.movies_rw);


        mMoviesAdapter = new MoviesAdapter(this);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecycleViewMovies.setLayoutManager(mLayoutManager);
        mRecycleViewMovies.setAdapter(mMoviesAdapter);
        mRecycleViewMovies.addOnScrollListener(new MovieScrollPagination());

        downLoadItemsToAdapter(1);
        if (MOVIE_TYPE_POSITION == 0) {
            setTitle(R.string.popular_movie);
        } else if (MOVIE_TYPE_POSITION == 1) {
            setTitle(R.string.top_rated);
        }


    }

    @SuppressLint("StaticFieldLeak")
    private void downLoadItemsToAdapter(int page) {
        try {
            final URL url = MovieConnectionUtils.buildMovieUrl(MOVIE_TYPE_POSITION, page);
            mTask = new AsyncTask<URL, String, MovieItem[]>() {
                @Override
                protected void onPreExecute() {
                    mStatusNode.setVisibility(View.VISIBLE);
                    mStatusProgressBar.setVisibility(View.VISIBLE);
                    mStatusTextView.setVisibility(View.INVISIBLE);
                }

                @Override
                protected void onPostExecute(MovieItem[] movieItems) {
                    if (movieItems != null) {
                        mMoviesAdapter.addAllToCollection(movieItems);
                        mStatusNode.setVisibility(View.GONE);
                    } else {
                        if (mErrorMessage != null) {
                            showError(mErrorMessage);
                            mErrorMessage = null;
                        }
                    }
                    isLoading = false;

                }

                @Override
                protected MovieItem[] doInBackground(URL... urls) {
                    MovieItem[] result = null;
                    isLoading = true;
                    try {
                        HttpsURLConnection c = null;
                        InputStream stream = null;

                        try {
                            c = (HttpsURLConnection) urls[0].openConnection();
                            c.setRequestMethod("GET");
                            c.connect();
                            int responseCode = c.getResponseCode();
                            if (responseCode != HttpsURLConnection.HTTP_OK) {
                                throw new Exception("Code:" + responseCode + " ");
                            }
                            stream = c.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                sb.append(line);
                            }

                            JSONObject body = new JSONObject(sb.toString());

                            int total_pages = body.getInt("total_pages");
                            mMoviesAdapter.mPages = total_pages;
                            mMoviesAdapter.mPage = body.getInt("page");
                            JSONArray results = body.getJSONArray("results");
                            MovieItem[] items = new MovieItem[results.length()];
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);
                                MovieItem movieItem = new MovieItem(movie.getString("original_title"),
                                        movie.getString("poster_path"),
                                        movie.getString("overview"),
                                        movie.getDouble("vote_average"),
                                        movie.getString("release_date"));
                                items[i] = movieItem;
                            }
                            result = items;


                        } finally {
                            if (stream != null) {
                                stream.close();
                            }
                            if (c != null) {
                                c.disconnect();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        mErrorMessage = e.getMessage();
                    }
                    return result;
                }
            };
            mTask.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }

    }

    @Override
    public void onClickItem(MovieItem item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", item.mOriginalTitle);
        intent.putExtra("rating", item.mVoteAverage.toString());
        intent.putExtra("release_date", item.mReleaseDate);
        intent.putExtra("overview", item.mOverView);
        intent.putExtra("poster", item.mImageUrl);
        startActivity(intent);

    }

    public void showError(String error) {

        if (mErrorMessage != null) {
            mStatusTextView.setText(error);
            mStatusNode.setVisibility(View.VISIBLE);
            mStatusProgressBar.setVisibility(View.INVISIBLE);
            mStatusTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_menu) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieScrollPagination extends RecyclerView.OnScrollListener {


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if ((dx == 0 && dy == 0) || dy < 0) return;
            int visibleItems = mLayoutManager.getChildCount();
            int totalItems = mMoviesAdapter.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (isLoading) return;

            if (mMoviesAdapter.mPage < mMoviesAdapter.mPages && (visibleItems + firstVisibleItemPosition + 4) > totalItems) {
                downLoadItemsToAdapter(mMoviesAdapter.mPage + 1);
            }
        }
    }
}
