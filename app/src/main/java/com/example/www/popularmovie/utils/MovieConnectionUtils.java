package com.example.www.popularmovie.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.www.popularmovie.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MovieConnectionUtils {
    static final String SCHEME = "https";
    static final String AUTHORITY = "api.themoviedb.org";
    static final String API_VERSION = "3";
    static final String API_KEY = BuildConfig.API_KEY;


    public static URL buildMovieUrl(int type, @Nullable Integer page)
            throws MalformedURLException {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(SCHEME);
        uriBuilder.authority(AUTHORITY);
        uriBuilder.appendPath(API_VERSION);
        uriBuilder.appendPath("movie");

        switch (type) {
            case 0:
                uriBuilder.appendPath("popular");
                break;
            case 1:
                uriBuilder.appendPath("top_rated");
                break;
            default:
                Log.d(MovieConnectionUtils.class.getName(), type + "");
                throw new IllegalArgumentException("Provided illegal type: " + type);
        }
        uriBuilder.appendQueryParameter("api_key", API_KEY + "");
        if (page != null) {
            uriBuilder.appendQueryParameter("page", Integer.toString(page));
        }
        return new URL(uriBuilder.build().toString());
    }


}


