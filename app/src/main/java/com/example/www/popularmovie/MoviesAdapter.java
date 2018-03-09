package com.example.www.popularmovie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by mo on 21.02.18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final static int ITEMS_PER_PAGE = 20;
    public int mPages = 0;
    public int mPage = 0;
    private List<MovieItem> mMoviesCollection = new ArrayList<>(ITEMS_PER_PAGE);
    private OnItemClickListener mListener;

    MoviesAdapter() {
        super();
    }

    MoviesAdapter(OnItemClickListener listener) {
        this();
        mListener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesAdapter.ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieItem item = mMoviesCollection.get(position);
        ViewHolderItem itemHolder = (ViewHolderItem) holder;
        itemHolder.mOriginalTitle_tv.setText(item.mOriginalTitle);
        String url = "http://image.tmdb.org/t/p/w185/" + item.mImageUrl;

        Picasso.with(itemHolder.mPoster_iv.getContext())
                .load(url)
                .into(itemHolder.mPoster_iv);
    }


    @Override
    public int getItemCount() {

        return mMoviesCollection.size();
    }


    public void addAllToCollection(MovieItem... items) {

        int start = mMoviesCollection.size();
        int count = items.length;
        if (Collections.addAll(mMoviesCollection, items)) {
            notifyItemRangeInserted(start, count);
        }
    }

    interface OnItemClickListener {
        void onClickItem(MovieItem item);
    }

    class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPoster_iv;
        TextView mOriginalTitle_tv;

        ViewHolderItem(View v) {
            super(v);
            mPoster_iv = v.findViewById(R.id.poster_iv);
            mOriginalTitle_tv = v.findViewById(R.id.original_title_tv);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickItem(mMoviesCollection.get(getAdapterPosition()));
        }
    }
}
