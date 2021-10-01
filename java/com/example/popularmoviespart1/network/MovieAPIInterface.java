package com.example.popularmoviespart1.network;

import com.example.popularmoviespart1.BuildConfig;
import com.example.popularmoviespart1.model.CommentResponsePage;
import com.example.popularmoviespart1.model.MovieResponsePage;
import com.example.popularmoviespart1.model.TrailerResponsePage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPIInterface {
    // define url in two parts
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY_PARAM = "api_key";
    String API_KEY = BuildConfig.API_KEY;
    String SORT_BY_PARAM = "sort_by";
    String SORT_BY_KEY = "original_title.asc";

    @GET("discover/movie")
    Call<MovieResponsePage> getDefaultMovies(@Query(API_KEY_PARAM) String API_KEY,
                                             @Query(SORT_BY_PARAM) String SORT_BY_KEY);


    @GET("movie/popular")
    Call<MovieResponsePage> getPopularMovies(@Query(API_KEY_PARAM) String API_KEY);

    @GET("movie/top_rated")
    Call<MovieResponsePage> getRatedMovies(@Query(API_KEY_PARAM) String API_KEY);

    @GET("movie/{id}/reviews")
    Call<CommentResponsePage> getComments(@Path("id") Integer id,
                                          @Query(API_KEY_PARAM) String API_KEY);

    @GET("movie/{id}/videos")
    Call<TrailerResponsePage> getTrailers(@Path("id") Integer id,
                                          @Query(API_KEY_PARAM) String API_KEY);


}
