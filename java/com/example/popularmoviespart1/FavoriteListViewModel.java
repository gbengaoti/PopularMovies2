package com.example.popularmoviespart1;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmoviespart1.database.AppDatabase;
import com.example.popularmoviespart1.database.FavoriteEntry;

import java.util.List;

public class FavoriteListViewModel extends AndroidViewModel {

    private static final String TAG = FavoriteListViewModel.class.getSimpleName();

    private final LiveData<List<FavoriteEntry>> favorites;

    public FavoriteListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        //Log.d(TAG, "Actively retrieving the tasks from the DataBase");#
        favorites = database.favoriteDao().loadallFavorites();
    }

    LiveData<List<FavoriteEntry>> getFavorites(){
        return favorites;
    }

}
