package com.example.www.popularmovie.utils;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mo on 24.02.18.
 */

public class MovieConnectionUtilsInstTest {
    @Test
    public void buildMovieUrlTest() {
        try {
            URL expectedPopularMovieURL = new URL(MovieConnectionUtils.SCHEME + "://" +
                    MovieConnectionUtils.AUTHORITY + "/" +
                    MovieConnectionUtils.API_VERSION +
                    "/movie/popular?api_key=" + MovieConnectionUtils.API_KEY);
            URL actualPopularMovieURL = MovieConnectionUtils.buildMovieUrl(0, null);
            assertEquals(expectedPopularMovieURL, actualPopularMovieURL);

            URL expectedTopRatedMovieURL = new URL(MovieConnectionUtils.SCHEME + "://" +
                    MovieConnectionUtils.AUTHORITY + "/" +
                    MovieConnectionUtils.API_VERSION +
                    "/movie/top_rated?api_key=" + MovieConnectionUtils.API_KEY);
            URL actualTopRatedMovieURL = MovieConnectionUtils.buildMovieUrl(1, null);
            assertEquals(expectedTopRatedMovieURL, actualTopRatedMovieURL);
            assertNotEquals(expectedTopRatedMovieURL, actualPopularMovieURL);

            URL expectedTopRatedMovieURLPage_2 = new URL(MovieConnectionUtils.SCHEME + "://" +
                    MovieConnectionUtils.AUTHORITY + "/" +
                    MovieConnectionUtils.API_VERSION +
                    "/movie/top_rated?api_key=" + MovieConnectionUtils.API_KEY + "&page=2");
            URL actualTopRatedMovieURLPage_2 = MovieConnectionUtils.buildMovieUrl(1, 2);
            assertEquals(expectedTopRatedMovieURLPage_2, actualTopRatedMovieURLPage_2);

        } catch (MalformedURLException e) {
            assertTrue(false);
        }


    }
}
