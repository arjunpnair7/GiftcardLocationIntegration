package com.example.giftcardlocationintegration.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.giftcardlocationintegration.Giftcard;

import java.util.List;

@Dao
public interface GiftCardDao {

    @Insert
    public void addNewGiftCard(Giftcard giftCard);

    @Query("SELECT * FROM giftcard")
    public List<Giftcard> getAllGiftCards();
}
