package com.example.www.popularmovie;


public class MovieItem {

    public String mOriginalTitle;
    public String mImageUrl;
    public String mOverView;
    public Double mVoteAverage;
    public String mReleaseDate;

    public MovieItem() {
    }


    public MovieItem(String title, String image, String overview, Double vote, String releaseDate) {
        mOriginalTitle = title;
        mImageUrl = image;
        mOverView = overview;
        mVoteAverage = vote;
        /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(releaseDate);
        } catch (ParseException e) {
            Log.d(this.getClass().getName(), e.toString());
        }*/
        mReleaseDate = releaseDate;


    }
}
