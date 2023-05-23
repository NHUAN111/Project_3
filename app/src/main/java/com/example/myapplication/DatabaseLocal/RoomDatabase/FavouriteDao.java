package com.example.myapplication.DatabaseLocal.RoomDatabase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.Models.Favourite;

import java.util.List;

@androidx.room.Dao
public interface FavouriteDao {
    @Insert
    void insertFavorite(Favourite favorite);
    @Delete
    void deleteFavorite(Favourite favorite);

    @Query("SELECT * FROM favorite WHERE customer_id= :customer_id")
    List<Favourite> getListFavorite(int customer_id);

    @Query("SELECT * FROM favorite WHERE customer_id=:customer_id AND food_name=:food_name")
    Favourite getFavorite(String food_name, int customer_id);

    @Query("SELECT * FROM favorite WHERE favorite_id=:favorite_id")
    Favourite getFavoriteByFavoriteId(int favorite_id);
}
