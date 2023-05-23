package com.example.myapplication.DatabaseLocal.RoomDatabase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Models.Cart;
import com.example.myapplication.Models.Delivery;

import java.util.List;
@androidx.room.Dao
public interface DeliveryDao {
    @Insert
    void insertDelivery(Delivery delivery);
    @Delete
    void deleteDelivery(Delivery delivery);
    @Update
    void updateDelivery(Delivery delivery);

    @Query("DELETE FROM delivery WHERE customer_id = :customerId")
    void deleteAllDelivery(int customerId);

    @Query("SELECT * FROM delivery WHERE customer_id=:customer_id AND status_delivery=:status ORDER BY id DESC LIMIT 1;")
    Delivery searchDelivery(int customer_id, boolean status);

    @Query("SELECT * FROM delivery WHERE customer_id= :customer_id")
    List<Delivery> getListDelivery(int customer_id);

    @Query("SELECT * FROM delivery WHERE customer_id=:customer_id AND status_delivery=:status_delivery")
    Delivery getDelivery(int customer_id, boolean status_delivery);

    @Query("SELECT * FROM delivery WHERE id=:id_delivery")
    Delivery getDeliveryByDeliveryId(int id_delivery);
}
