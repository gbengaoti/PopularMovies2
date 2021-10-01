package com.example.popularmoviespart1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviespart1.database.FavoriteEntry;
import com.example.popularmoviespart1.model.Movie;
import com.example.popularmoviespart1.model.MovieResponsePage;
import com.example.popularmoviespart1.network.APIMovieClient;
import com.example.popularmoviespart1.network.MovieAPIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.popularmoviespart1.network.MovieAPIInterface.API_KEY;
import static com.example.popularmoviespart1.network.MovieAPIInterface.SORT_BY_KEY;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, OnTaskCompleted {

    private RecyclerView mMovieRecyclerView;
    private TextView mErrorTextView;
    private MovieAdapter mMovieAdapter;
    private final String choiceKey = "CHOICE";
    private final String ERROR_MESSAGE = "Failure to get movies";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieRecyclerView = findViewById(R.id.my_recyler_view);
        mErrorTextView = findViewById(R.id.error_text_view);

        mMovieRecyclerView.setHasFixedSize(true);

        int NUMBER_OF_COLUMNS;
        RecyclerView.LayoutManager layoutManager;
        if (this.getResources().getBoolean(R.bool.is_landscape)){
            NUMBER_OF_COLUMNS = 3;
            layoutManager = (new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        }else{
            NUMBER_OF_COLUMNS = 2;
            layoutManager = (new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        }

        mMovieRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",
                MODE_PRIVATE);

        String DEFAULT = "default";
        String FAVORITE = "favorite";
        String choice = sharedPreferences.getString(choiceKey, DEFAULT);

        if (choice.equals(FAVORITE)){
            loadFavoriteData();
        }else{
            getMovies(choice);
        }


    }

    private void showError(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mMovieRecyclerView.setVisibility(View.INVISIBLE);

    }

    private void showRecyclerView(){
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie currentMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, currentMovie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onTaskCompleted(Movie[] movies) {
        if (movies != null){
            showRecyclerView();
            mMovieAdapter.setMovieData(movies);
        }else {
            showError();
        }
    }

    public void getMovies(String choice){
        final String DEFAULT = "default";
        final String POPULAR = "popular";
        final String RATING = "rated";
        Retrofit retrofit = APIMovieClient.getRetrofitInstance();

        MovieAPIInterface apiService = retrofit.create(MovieAPIInterface.class);

        Call<MovieResponsePage> call;
        switch (choice) {
            case POPULAR:
                //load popular movies
                call = apiService.getPopularMovies(API_KEY);
                break;
            case RATING:
                call = apiService.getRatedMovies(API_KEY);
                break;
            case DEFAULT:
                call = apiService.getDefaultMovies(API_KEY, SORT_BY_KEY);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }


        call.enqueue(new Callback<MovieResponsePage>() {
            @Override
            public void onResponse(Call<MovieResponsePage> call,
                                   Response<MovieResponsePage> response) {
                //Log.v(TAG, String.valueOf(response.code()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    final List<Movie> movies = response.body().getResults();
                    if (movies != null) {
                        onTaskCompleted(movies.toArray(new Movie[0]));
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieResponsePage> call, Throwable t) {
                //Log.v(TAG, ERROR_MESSAGE);
            }
        });

    }



    private void loadMovieData(String option){
        mMovieAdapter.setMovieData(null);
        getMovies(option);
        showRecyclerView();
    }

    private void loadFavoriteData(){
        mMovieAdapter.setMovieData(null);
        setupViewModel();
        showRecyclerView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selector_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",
                MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        String mCurrentChoice;
        if (id == R.id.action_sort_popular) {
            String POPULAR = "popular";
            mCurrentChoice = POPULAR;
            myEdit.putString(choiceKey, mCurrentChoice);
            myEdit.apply();
            loadMovieData(POPULAR);
            return true;
        }

        if(id == R.id.action_sort_rating){
            String RATING = "rated";
            mCurrentChoice = RATING;
            myEdit.putString(choiceKey, mCurrentChoice);
            myEdit.apply();
            loadMovieData(RATING);
            return true;
        }

        if(id == R.id.action_sort_favorite){
            //Intent intent = new Intent(MainActivity.this,FavoriteActivity.class);
            //startActivity(intent);
            String FAVORITE = "favorite";
            myEdit.putString(choiceKey, FAVORITE);
            myEdit.apply();
            setupViewModel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Movie[] covertFavoriteEntries(List<FavoriteEntry> favoriteEntries){
        ArrayList<Movie> movieList = new ArrayList<>();
        for (FavoriteEntry entry: favoriteEntries){
            movieList.add(new Movie(entry));
        }

        return movieList.toArray(new Movie[0]);
    }

    public void setupViewModel() {

        FavoriteListViewModel viewModel =
                ViewModelProviders.of(this).get(FavoriteListViewModel.class);
        viewModel.getFavorites().observe(this,
                favoriteEntries -> mMovieAdapter.setMovieData(covertFavoriteEntries(favoriteEntries)));
    }


}