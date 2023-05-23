package com.example.myapplication.DatabaseLocal.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.myapplication.Models.Favourite;

@androidx.room.Database(entities = {Favourite.class}, version = 1)
public abstract class DatabaseFavourite extends RoomDatabase {
    private static DatabaseFavourite instance;
    public abstract FavouriteDao dao();

    public static synchronized DatabaseFavourite getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseFavourite.class, "favorite")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // this method is called when database is created
            // and below line is to populate our data.
            new DatabaseFavourite.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(DatabaseFavourite instance) {
            FavouriteDao dao = instance.dao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
