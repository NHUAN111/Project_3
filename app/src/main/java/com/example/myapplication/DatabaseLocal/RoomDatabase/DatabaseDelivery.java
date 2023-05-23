package com.example.myapplication.DatabaseLocal.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Models.Delivery;
@androidx.room.Database(entities = {Delivery.class}, version = 2)
public abstract class DatabaseDelivery extends RoomDatabase {
    private static DatabaseDelivery instance;
    public abstract DeliveryDao dao();

    public static synchronized DatabaseDelivery getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseDelivery.class, "delivery")
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
            new DatabaseDelivery.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(DatabaseDelivery instance) {
            DeliveryDao dao = instance.dao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
