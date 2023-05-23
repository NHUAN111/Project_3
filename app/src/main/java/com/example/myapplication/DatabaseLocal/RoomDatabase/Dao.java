package com.example.myapplication.DatabaseLocal.RoomDatabase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Models.Cart;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert
    void insert(Cart model);

    @Update
    void update(Cart model);

    @Delete
    void delete(Cart model);

    @Query("DELETE FROM cart_table WHERE customer_id = :customerId AND nameFood = :nameFood")
    void delete(int customerId, String nameFood);

    @Query("DELETE FROM cart_table WHERE customer_id = :customerId")
    void deleteAllCart(int customerId);

    @Query("SELECT * FROM cart_table WHERE nameFood=:nameFood AND customer_id=:customer_id ")
    Cart searchCart(String nameFood, int customer_id);

    @Query("SELECT * FROM cart_table WHERE customer_id = :customerId")
    List<Cart> getCartItemsForCustomer(int customerId);

}
